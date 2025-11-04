package linkProject.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import linkProject.models.Link;
import linkProject.models.User;
import linkProject.services.LinkService;
import linkProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class LinkController {
    @Autowired
    LinkService linkService;
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(HttpSession httpSession, Model model) {
        User user = null;
        String userUuid = (String) httpSession.getAttribute("userUuid");
        if (userUuid == null) {
            userUuid = UUID.randomUUID().toString();
            user = new User(userUuid);
            userService.save(user);
            httpSession.setAttribute("userUuid", userUuid);
        } else {
            user = userService.findByUuid(userUuid);
            if (user.getDeletedLinksCount() > 0) {
                model.addAttribute("showAlertExp", true);
                user.setDeletedLinksCount(0);
                userService.save(user);
            }
        }
        List<Link> links = linkService.getUserLinks(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("links", links);
        return "home";
    }

    @PostMapping("/")
    public String convertUrl(@RequestParam String long_url, @RequestParam String count_click, HttpSession httpSession, Model model) {

        int countClick = 0;
        try {
            new URL(long_url).toURI();
            countClick = Integer.parseInt(count_click);
            if (countClick < 0) {
                throw new NumberFormatException();
            }
        } catch (MalformedURLException | URISyntaxException e) {
            model.addAttribute("showAlertWrongUrl", true);
            return home(httpSession, model);
        } catch (NumberFormatException e) {
            model.addAttribute("showAlertInvalidNum", true);
            return home(httpSession, model);
        }

        String userUuid = (String) httpSession.getAttribute("userUuid");
        User user = userService.findByUuid(userUuid);

        Link link = linkService.createShortLink(long_url, user.getId(), countClick);
        linkService.save(link);
        System.out.println(link);

        return home(httpSession, model);
    }

    @GetMapping("/redir/**")
    public String redirect(HttpServletRequest request, HttpSession httpSession, Model model) {
        String path = request.getRequestURI().substring("/redir/".length());
        System.out.println(path);
        Optional<String> messageOpt = linkService.redirect(path);
        if (messageOpt.isEmpty()) {
            return "redirect:/?error=not_found";
        }
        String message = messageOpt.get();
        if (message.contains("Expired")) {
            model.addAttribute("showAlertExp", true);
            return home(httpSession, model);
        } else if (message.contains("Limit")) {
            model.addAttribute("showAlertLim", true);
            return home(httpSession, model);
        }
        return "redirect:" + message;
    }

}
