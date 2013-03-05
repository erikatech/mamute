package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.auth.LoggedAccess;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.dao.FlagDao;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.FlagType;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class FlagController {
	
	private final Result result;
	private final CommentDAO comments;
	private final FlagDao flags;
	private final LoggedUser loggedUser;

	public FlagController(Result result, CommentDAO comments, FlagDao flags, LoggedUser loggedUser) {
		this.result = result;
		this.comments = comments;
		this.flags = flags;
		this.loggedUser = loggedUser;
	}

	@LoggedAccess
	@Post("/comments/{commentId}/flags")
	public void addFlag(Long commentId, FlagType flagType) {
		Comment comment = comments.getById(commentId);
		Flag flag = new Flag(flagType, loggedUser.getCurrent());
		flags.save(flag);
		comment.add(flag);
		
		result.nothing();
	}
}
