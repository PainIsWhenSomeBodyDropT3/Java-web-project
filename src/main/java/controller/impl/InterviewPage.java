package controller.impl;

import bean.Role;
import bean.User;
import bean.Vacancy;
import controller.Command;
import controller.PageAccess;
import exception.CommandException;
import exception.ServiceException;
import service.ServiceFactory;
import service.UserService;
import service.VacancyService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InterviewPage extends PageAccess implements Command {
    private UserService userService = ServiceFactory.getInstance().getUserService();
    private VacancyService vacancyService = ServiceFactory.getInstance().getVacancyService();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws CommandException {
        try {
            User user = isLogin(req);
            if (user != null && user.getRole() == Role.HR) {

                int vacId = Integer.parseInt(req.getParameter("vacId"));
                int seekerId = Integer.parseInt(req.getParameter("seekerId"));

                User seeker = new User(seekerId);
                Vacancy vacancy = new Vacancy(vacId);
                User hr = (User) req.getSession().getAttribute("user");

                seeker = userService.readById(seeker.getId());
                vacancy = vacancyService.readById(vacancy.getId());

                req.setAttribute("seeker", seeker);
                req.setAttribute("vacancy", vacancy);
                req.setAttribute("hr", hr);
                req.getRequestDispatcher("/interviewPage.jsp").forward(req, resp);

            } else {
                resp.sendRedirect("/login.jsp");
            }
        } catch (IOException | ServiceException | ServletException e) {
            throw new CommandException(e);
        }
    }

    @Override
    public User isLogin(HttpServletRequest request) {
        return checkLogin(request);
    }
}