/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jasig.ssp.model.external.Term;
import org.jasig.ssp.transferobject.PlanCourseTO;

public class TermCourses {
	
	public static class TermCoursesTermDateComparator implements Comparator<TermCourses> {
		 @Override
		    public int compare(TermCourses o1, TermCourses o2) {
		        return o1.getTerm().getStartDate().compareTo( o2.getTerm().getStartDate());
		    }

	}

	public static final TermCoursesTermDateComparator TERM_START_DATE_COMPARATOR =
			new TermCoursesTermDateComparator();
	
	private Term term;
	private List<PlanCourseTO> courses;
	private Float totalCreditHours;
	private Float totalDevCreditHours;
	private String contactNotes;
	private String studentNotes;
	private Boolean isImportant;
	private Boolean isElective;
	
	
	/**
	 * @return the term
	 */
	public Term getTerm() {
		return term;
	}
	/**
	 * @param term the term to set
	 */
	public void setTerm(Term term) {
		this.term = term;
	}
	/**
	 * @return the courses
	 */
	public List<PlanCourseTO> getCourses() {
		return courses;
	}
	/**
	 * @param courses the courses to set
	 */
	public void setCourses(List<PlanCourseTO> courses) {
		this.courses = courses;
		updateCreditHours();
	}

	public void addCourse(PlanCourseTO planCourse){
		courses.add(planCourse);
		updateCreditHours();
	}
	
	private void updateCreditHours(){
		totalCreditHours = new Float(0);
		totalDevCreditHours = new Float(0);
		for(PlanCourseTO course:courses){
			totalCreditHours = totalCreditHours + new Float(course.getCreditHours());
			if(course.isDev())
				totalDevCreditHours = totalDevCreditHours + new Float(course.getCreditHours());
		}
	}
	/**
	 * @param term
	 */
	public TermCourses(Term term) {
		super();
		this.term = term;
		this.courses = new ArrayList<PlanCourseTO>();
	}
	
	/**
	 * @return the totalCreditHours
	 */
	public Float getTotalCreditHours() {
		return totalCreditHours;
	}
	
	/**
	 * @param totalDevCreditHours the totalDevCreditHours to set
	 */
	public void setTotalDevHours(Float totalDevCreditHours) {
		this.totalDevCreditHours = totalDevCreditHours;
	}
	
	/**
	 * @return the totalDevCreditHours
	 */
	public Float getTotalDevCreditHours() {
		return totalDevCreditHours;
	}
	
	/**
	 * @param totalCreditHours the totalCreditHours to set
	 */
	public void setTotalCreditHours(Float totalCreditHours) {
		this.totalCreditHours = totalCreditHours;
	}
	/**
	 * @return the contactNotes
	 */
	public String getContactNotes() {
		return contactNotes;
	}
	/**
	 * @param contactNotes the contactNotes to set
	 */
	public void setContactNotes(String contactNotes) {
		this.contactNotes = contactNotes;
	}
	/**
	 * @return the studentNotes
	 */
	public String getStudentNotes() {
		return studentNotes;
	}
	/**
	 * @param studentNotes the studentNotes to set
	 */
	public void setStudentNotes(String studentNotes) {
		this.studentNotes = studentNotes;
	}
	/**
	 * @return the isImportant
	 */
	public Boolean getIsImportant() {
		return isImportant;
	}
	/**
	 * @param isImportant the isImportant to set
	 */
	public void setIsImportant(Boolean isImportant) {
		this.isImportant = isImportant;
	}
	/**
	 * @return the isElective
	 */
	public Boolean getIsElective() {
		return isElective;
	}
	/**
	 * @param isElective the isElective to set
	 */
	public void setIsElective(Boolean isElective) {
		this.isElective = isElective;
	}
	/**
	 * @param totalDevCreditHours the totalDevCreditHours to set
	 */
	public void setTotalDevCreditHours(Float totalDevCreditHours) {
		this.totalDevCreditHours = totalDevCreditHours;
	}

}