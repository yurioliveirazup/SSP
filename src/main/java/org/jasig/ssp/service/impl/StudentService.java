package org.jasig.ssp.service.impl;

import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.model.PersonProgramStatus;
import org.jasig.ssp.model.reference.ProgramStatus;
import org.jasig.ssp.model.reference.StudentType;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonProgramStatusService;
import org.jasig.ssp.service.reference.ProgramStatusService;
import org.jasig.ssp.service.reference.StudentTypeService;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@Service
@Transactional
class StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private transient PersonProgramStatusService personProgramStatusService;
    @Autowired
    private transient ProgramStatusService programStatusService;
    @Autowired
    private transient StudentTypeService studentTypeService;

    public void ensureValidAlertedOnPersonStateNoFail(Person person) {
        try {
            ensureValidAlertedOnPersonStateOrFail(person);
        } catch ( Exception e ) {
            LOGGER.error("Unable to set a program status or student type on "
                    + "person '{}'. This is likely to prevent that person "
                    + "record from appearing in caseloads, student searches, "
                    + "and some reports.", person.getId(), e);
        }
    }

    private void ensureValidAlertedOnPersonStateOrFail(Person person)
            throws ObjectNotFoundException, ValidationException {

        if ( person.getObjectStatus() != ObjectStatus.ACTIVE ) {
            person.setObjectStatus(ObjectStatus.ACTIVE);
        }

        final ProgramStatus programStatus =  programStatusService.getActiveStatus();
        if ( programStatus == null ) {
            throw new ObjectNotFoundException(
                    "Unable to find a ProgramStatus representing \"activeness\".",
                    "ProgramStatus");
        }

        Set<PersonProgramStatus> programStatuses =
                person.getProgramStatuses();
        if ( programStatuses == null || programStatuses.isEmpty() ) {
            PersonProgramStatus personProgramStatus = new PersonProgramStatus();
            personProgramStatus.setEffectiveDate(new Date());
            personProgramStatus.setProgramStatus(programStatus);
            personProgramStatus.setPerson(person);
            programStatuses.add(personProgramStatus);
            person.setProgramStatuses(programStatuses);
            // save should cascade, but make sure custom create logic fires
            personProgramStatusService.create(personProgramStatus);
        }

        if ( person.getStudentType() == null ) {
            StudentType studentType = studentTypeService.get(StudentType.EAL_ID);
            if ( studentType == null ) {
                throw new ObjectNotFoundException(
                        "Unable to find a StudentType representing an early "
                                + "alert-assigned type.", "StudentType");
            }
            person.setStudentType(studentType);
        }
    }
}
