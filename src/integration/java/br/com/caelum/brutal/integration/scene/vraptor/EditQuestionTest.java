package br.com.caelum.brutal.integration.scene.vraptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;

import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class EditQuestionTest extends CustomVRaptorIntegration {

	@Test
	public void should_edit_question_of_other_author() throws Exception {
		Question question = createQuestionWithDao(moderator(),
				"Title title title title title title title",
				"Description description description description description", tag("java"));

		String newTitle = "NEW title title title title title title title";
		String newDescription = "NEW description description description description description";
		UserFlow navigation = login(navigate(), "karma.nigga@caelum.com.br");
		navigation = editQuestionWithFlow(navigation, question.getId(),
				newTitle, newDescription, "edited question woots!", "java");
		
		VRaptorTestResult editedQuestion = navigation.followRedirect().execute();
		editedQuestion.wasStatus(200).isValid();
		
		List<String> messagesList = messagesList(editedQuestion);
		assertTrue(messagesList.contains(message("status.pending")));
	}
	
	@Test
	public void should_edit_and_automatically_approve_author_edit() throws Exception {
		Question question = createQuestionWithDao(karmaNigga(),
				"Title title title title title title title",
				"Description description description description description", tag("java"));

		String newTitle = "NEW title title title title title title title";
		String newDescription = "NEW description description description description description";
		UserFlow navigation = login(navigate(), "karma.nigga@caelum.com.br");
		navigation = editQuestionWithFlow(navigation, question.getId(),
				newTitle, newDescription, "edited question woots!", "java");
		
		VRaptorTestResult editedQuestion = navigation.followRedirect().execute();
		editedQuestion.wasStatus(200).isValid();
		
		List<String> messagesList = messagesList(editedQuestion);
		assertTrue(messagesList.contains(message("status.no_need_to_approve")));

		Question fetchQuestion = editedQuestion.getObject("question");
		assertEquals(newTitle, fetchQuestion.getTitle());
		assertEquals(newDescription, fetchQuestion.getDescription());
	}
	
	@Test
	public void should_edit_and_automatically_approve_moderator() throws Exception {
		Question question = createQuestionWithDao(karmaNigga(),
				"Title title title title title title title",
				"Description description description description description", tag("java"));

		String newTitle = "NEW title title title title title title title";
		String newDescription = "NEW description description description description description";
		UserFlow navigation = login(navigate(), "moderator@caelum.com.br");
		navigation = editQuestionWithFlow(navigation, question.getId(),
				newTitle, newDescription, "edited question woots!", "java");
		
		VRaptorTestResult editedQuestion = navigation.followRedirect().execute();
		editedQuestion.wasStatus(200).isValid();
		
		List<String> messagesList = messagesList(editedQuestion);
		assertTrue(messagesList.contains(message("status.no_need_to_approve")));

		Question fetchQuestion = editedQuestion.getObject("question");
		assertEquals(newTitle, fetchQuestion.getTitle());
		assertEquals(newDescription, fetchQuestion.getDescription());
	}
	
	@Ignore
	@Test
	public void should_touch_question() throws Exception {
		User user = randomUser();
		
		Question question = createQuestionWithDao(user(user.getEmail()), "Question question question question question",
				"Description description description description description", tag("java"));
		
		UserFlow navigation = login(navigate(), user.getEmail());
		navigation = editQuestionWithFlow(navigation, question.getId(), "ASdoA sodi aosido iasod iOASIDoIASOdi", "asd oiasudo iausdoi uasoid uaosiduasoduoasi udaiosud oiasud oiasud oisa", "so diaos diaosi d", "java");
		VRaptorTestResult editedQuestion = navigation.followRedirect().execute();
		editedQuestion.wasStatus(200).isValid();
		
		Elements questionElement = getElementsByClass(editedQuestion.getResponseBody(),
				"edited-touch");
//		assertTrue(questionElement.);

//		WebElement edited = byClassName("post-touchs").findElement(By.cssSelector(".touch.edited-touch"));
//		return isElementPresent(tagName("img"), edited);
		}

}
