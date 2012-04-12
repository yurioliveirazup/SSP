package org.studentsuccessplan.mygps.model.transferobject;

import java.util.List;

import org.studentsuccessplan.ssp.transferobject.reference.SelfHelpGuideTO;

public class SelfHelpGuideContentTO extends SelfHelpGuideTO {

	private String introductoryText;
	private List<SelfHelpGuideQuestionTO> questions;

	public String getIntroductoryText() {
		return introductoryText;
	}

	public void setIntroductoryText(String introductoryText) {
		this.introductoryText = introductoryText;
	}

	public List<SelfHelpGuideQuestionTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<SelfHelpGuideQuestionTO> questions) {
		this.questions = questions;
	}

}
