package controller.impl;

import bean.User;
import controller.Command;
import controller.CommandName;
import controller.CommandProvider;
import exception.CommandException;
import exception.ServiceException;
import service.ServiceFactory;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Login implements Command {
    private UserService userService = ServiceFactory.getInstance().getUserService();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        HttpSession session = req.getSession();

        try {
            String username = req.getParameter("name");
            String password = req.getParameter("password");
            User user = new User(username, password);
            if (userService.isExist(user)) {

                user = userService.getUserByName(username);
                session.setAttribute("user", user);

                Command command = CommandProvider.getInstance().getCommand(CommandName.PROFILE_PAGE.name());
                command.execute(req,resp);

            } else {
                RequestDispatcher dispatcher = req.getRequestDispatcher("/login.jsp");
                req.setAttribute("message", "Not correct login or password");
                dispatcher.forward(req, resp);
            }
        } catch (IOException | ServletException | ServiceException e) {
            throw new CommandException("Error in redirect to page", e);
        }
    }
}