/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.model;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.jasig.ssp.model.reference.Campus;
import org.jasig.ssp.model.reference.EarlyAlertReason;
import org.jasig.ssp.model.reference.EarlyAlertSuggestion;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.PersonService;
import org.jasig.ssp.service.reference.EarlyAlertReasonService;
import org.jasig.ssp.service.reference.EarlyAlertSuggestionService;
import org.jasig.ssp.util.uuid.UUIDCustomType;

import com.google.common.collect.Sets;
import org.jasig.ssp.web.api.validation.ValidationException;

/**
 * EarlyAlert
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@TypeDef(name = "uuid-custom", typeClass = UUIDCustomType.class)
public class EarlyAlert // NOPMD by jon.adams on 5/24/12 1:29 PM
		extends AbstractAuditable
		implements PersonAssocAuditable { // NOPMD

	private static final long serialVersionUID = 8141595549982881039L;

	public static final String CUSTOM_GROUP_NAME = "Custom";

	@Column(nullable = true, length = 80)
	private String courseName;

	@Column(nullable = true, length = 255)
	private String courseTitle;

	@Column(nullable = true, length = 25)
	private String courseTermCode;

	@Column(name = "email_cc", nullable = true, length = 255)
	private String emailCC;

	@ManyToOne
	@JoinColumn(name = "campus_id", nullable = false)
	private Campus campus;

	@Column(nullable = true, length = 64000)
	@Size(max = 64000)
	private String earlyAlertReasonOtherDescription;

	@Column(nullable = true, length = 64000)
	@Size(max = 64000)
	private String earlyAlertSuggestionOtherDescription;
	
	@Column(name = "faculty_school_id", nullable = true, length = 255)
	private String facultySchoolId;
	
	@Column(name = "enrollment_status", nullable = true, length = 255)
	private String enrollmentStatus;	

	@Column(nullable = true, length = 64000)
	@Size(max = 64000)
	private String comment;

	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date closedDate;

	@ManyToOne
	@JoinColumn(name = "closed_by_id", nullable = true)
	private Person closedBy;
	
	private static final String RESPONSE_COUNT_FORMULA = "(select count(*) from early_alert_response ear where ear.early_alert_id = ID)";

	private static final String RESPONSE_DATES_FORMULA = "(select max(ear.modified_date) from early_alert_response ear "
			+ "where ear.early_alert_id = id)";

	
	@Formula(RESPONSE_COUNT_FORMULA)
	private int responseCount;
	
	@Formula(RESPONSE_DATES_FORMULA)
	private Date lastResponseDate;
	/**
	 * Associated person. Changes to this Person <i>are</i> persisted.
	 */
	@ManyToOne
	@JoinColumn(name = "person_id", nullable = false)
	private Person person;

	// TODO: eager loading makes more sense, but causes cartesian results. so
	// hold off optimizing performance until the performance pass of the system.
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "early_alert_early_alert_reason",
			joinColumns = @JoinColumn(name = "early_alert_id"),
			inverseJoinColumns = @JoinColumn(name = "early_alert_reason_id"))
	private Set<EarlyAlertReason> earlyAlertReasonIds = Sets.newHashSet();

	// TODO: eager loading makes more sense, but causes cartesian results. so
	// hold off optimizing performance until the performance pass of the system.
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "early_alert_early_alert_suggestion",
			joinColumns = @JoinColumn(name = "early_alert_id"),
			inverseJoinColumns = @JoinColumn(name = "early_alert_suggestion_id"))
	private Set<EarlyAlertSuggestion> earlyAlertSuggestions = Sets
			.newHashSet();

	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * @param courseName
	 *            the courseName to set; optional; max length 80 characters
	 */
	public void setCourseName(final String courseName) {
		this.courseName = courseName;
	}

	/**
	 * @return the courseTitle
	 */
	public String getCourseTitle() {
		return courseTitle;
	}

	/**
	 * @param courseTitle
	 *            the courseTitle to set; optional; max length 255 characters
	 */
	public void setCourseTitle(final String courseTitle) {
		this.courseTitle = courseTitle;
	}

	/**
	 * @return the courseTermCode
	 */
	public String getCourseTermCode() {
		return courseTermCode;
	}

	/**
	 * @param courseTermCode
	 *            the courseTermCode to set; optional; max length 25 characters
	 */
	public void setCourseTermCode(final String courseTermCode) {
		this.courseTermCode = courseTermCode;
	}

	/**
	 * @return the emailCC
	 */
	public String getEmailCC() {
		return emailCC;
	}

	/**
	 * @param emailCC
	 *            the emailCC to set; optional; max length 255 characters
	 */
	public void setEmailCC(final String emailCC) {
		this.emailCC = emailCC;
	}

	/**
	 * @return the campus
	 */
	public Campus getCampus() {
		return campus;
	}

	/**
	 * @param campus
	 *            the campus to set
	 */
	public void setCampus(final Campus campus) {
		this.campus = campus;
	}

	/**
	 * @return the earlyAlertReasonOtherDescription
	 */
	public String getEarlyAlertReasonOtherDescription() {
		return earlyAlertReasonOtherDescription;
	}

	/**
	 * @param earlyAlertReasonOtherDescription
	 *            the earlyAlertReasonOtherDescription to set; optional; max
	 *            length 64000 characters
	 */
	public void setEarlyAlertReasonOtherDescription(
			final String earlyAlertReasonOtherDescription) {
		this.earlyAlertReasonOtherDescription = earlyAlertReasonOtherDescription;
	}

	/**
	 * @return the earlyAlertSuggestionOtherDescription
	 */
	public String getEarlyAlertSuggestionOtherDescription() {
		return earlyAlertSuggestionOtherDescription;
	}

	/**
	 * @param earlyAlertSuggestionOtherDescription
	 *            the earlyAlertSuggestionOtherDescription to set; optional; max
	 *            length 64000 characters
	 */
	public void setEarlyAlertSuggestionOtherDescription(
			final String earlyAlertSuggestionOtherDescription) {
		this.earlyAlertSuggestionOtherDescription = earlyAlertSuggestionOtherDescription;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment; optional; max length 64000 characters
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * @return the closedDate
	 */
	public Date getClosedDate() {
		return closedDate == null ? null : new Date(closedDate.getTime());
	}

	/**
	 * @param closedDate
	 *            the closedDate to set
	 */
	public void setClosedDate(final Date closedDate) {
		this.closedDate = closedDate == null ? null : new Date(
				closedDate.getTime());
	}

	/**
	 * @return the closedById
	 */
	public UUID getClosedById() {
		return closedBy == null ? null : closedBy.getId();
	}

	public Person getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(Person closedBy) {
		this.closedBy = closedBy;
	}

	@Override
	public Person getPerson() {
		return person;
	}

	@Override
	public void setPerson(final Person person) {
		this.person = person;
	}

	/**
	 * @return the earlyAlertReasonIds
	 */
	public Set<EarlyAlertReason> getEarlyAlertReasons() {
		return earlyAlertReasonIds;
	}

	/**
	 * @param earlyAlertReasonIds
	 *            the earlyAlertReasonIds to set
	 */
	public void setEarlyAlertReasons(
			final Set<EarlyAlertReason> earlyAlertReasonIds) {
		this.earlyAlertReasonIds = earlyAlertReasonIds;
	}

	/**
	 * @return the earlyAlertSuggestionIds
	 */
	public Set<EarlyAlertSuggestion> getEarlyAlertSuggestions() {
		return earlyAlertSuggestions;
	}

	/**
	 * @param earlyAlertSuggestionIds
	 *            the earlyAlertSuggestionIds to set
	 */
	public void setEarlyAlertSuggestions(
			final Set<EarlyAlertSuggestion> earlyAlertSuggestionIds) {
		this.earlyAlertSuggestions = earlyAlertSuggestionIds;
	}

	@Override
	protected int hashPrime() {
		return 179;
	}

	@Override
	final public int hashCode() { // NOPMD by jon.adams on 5/9/12 1:50 PM
		int result = hashPrime();

		// Auditable properties
		result *= getId() == null ? "id".hashCode() : getId().hashCode();
		result *= getObjectStatus() == null ? hashPrime() : getObjectStatus()
				.hashCode();

		// EarlyAlert
		result *= hashField("courseName", courseName);
		result *= hashField("courseTitle", courseTitle);
		result *= hashField("courseTermCode", courseTermCode);
		result *= hashField("emailCC", emailCC);
		result *= hashField("campus", campus);
		result *= hashField("earlyAlertSuggestionOtherDescription",
				earlyAlertReasonOtherDescription);
		result *= hashField("comment", comment);
		result *= hashField("person", person);
		result *= hashField("closedDate", closedDate);
		result *= hashField("closedById", closedBy == null ? null : closedBy.getId());
		result *= hashField("facultySchoolId", facultySchoolId);
		result *= hashField("enrollmentStatus", enrollmentStatus);
	

		return result;
	}

	public int getResponseCount() {
		return responseCount;
	}

	public void setResponseCount(int responseCount) {
		this.responseCount = responseCount;
	}

	public Date getLastResponseDate() {
		return lastResponseDate;
	}

	public void setResponseDates(Date lastResponseDate) {
		this.lastResponseDate = lastResponseDate;
	}

	public String getFacultySchoolId() {
		return facultySchoolId;
	}

	public void setFacultySchoolId(String facultySchoolId) {
		this.facultySchoolId = facultySchoolId;
	}

	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public void setEnrollmentStatus(String enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}

	public UUID getStudentAdvisor() throws ValidationException {
		UUID assignedAdvisor = getEarlyAlertAdvisor();
		if (assignedAdvisor == null) {
			throw new ValidationException("Could not determine the Early Alert Advisor for student ID " + person.getId());
		}

		return assignedAdvisor;
	}

	/**
	 * Business logic to determine the advisor that is assigned to the student
	 * for this Early Alert.
	 *
	 * @throws ValidationException
	 *             If Early Alert, Student, and/or system information could not
	 *             determine the advisor for this student.
	 * @return The assigned advisor
	 */
	private UUID getEarlyAlertAdvisor()
			throws ValidationException {
		// Check for student already assigned to an advisor (a.k.a. coach)
		if ((getPerson().getCoach() != null) &&
				(getPerson().getCoach().getId() != null)) {
			return getPerson().getCoach().getId();
		}

		// Get campus Early Alert coordinator
		if (getCampus() == null) {
			throw new IllegalArgumentException("Campus ID can not be null.");
		}

		if (getCampus().getEarlyAlertCoordinatorId() != null) {
			// Return Early Alert coordinator UUID
			return getCampus().getEarlyAlertCoordinatorId();
		}

		// TODO If no campus EA Coordinator, assign to default EA Coordinator
		// (which is not yet implemented)

		// getEarlyAlertAdvisor should never return null
		throw new ValidationException(
				"Could not determined the Early Alert Coordinator for this student. Ensure that a default coordinator is set globally and for all campuses.");
	}

	public void updateFields(EarlyAlert obj,
							 PersonService personService,
							 EarlyAlertReasonService earlyAlertReasonService,
							 EarlyAlertSuggestionService earlyAlertSuggestionService) throws ObjectNotFoundException {
		this.setCourseName(obj.getCourseName());
		this.setCourseTitle(obj.getCourseTitle());
		this.setEmailCC(obj.getEmailCC());
		this.setCampus(obj.getCampus());
		this.setEarlyAlertReasonOtherDescription(obj
				.getEarlyAlertReasonOtherDescription());
		this.setComment(obj.getComment());
		this.setClosedDate(obj.getClosedDate());
		if ( obj.getClosedById() == null ) {
			this.setClosedBy(null);
		} else {
			this.setClosedBy(personService.get(obj.getClosedById()));
		}

		if (obj.getPerson() == null) {
			this.setPerson(null);
		} else {
			this.setPerson(personService.get(obj.getPerson().getId()));
		}

		final Set<EarlyAlertReason> earlyAlertReasons = new HashSet<EarlyAlertReason>();
		if (obj.getEarlyAlertReasons() != null) {
			for (final EarlyAlertReason reason : obj.getEarlyAlertReasons()) {
				earlyAlertReasons.add(earlyAlertReasonService.load(reason
						.getId()));
			}
		}

		this.setEarlyAlertReasons(earlyAlertReasons);

		final Set<EarlyAlertSuggestion> earlyAlertSuggestions = new HashSet<EarlyAlertSuggestion>();
		if (obj.getEarlyAlertSuggestions() != null) {
			for (final EarlyAlertSuggestion reason : obj
					.getEarlyAlertSuggestions()) {
				earlyAlertSuggestions.add(earlyAlertSuggestionService
						.load(reason
								.getId()));
			}
		}

		this.setEarlyAlertSuggestions(earlyAlertSuggestions);
	}
}