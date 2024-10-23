package org.ebndrnk.springbrauser.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Service
@RequiredArgsConstructor
public class PageParserService {
    private final RestTemplate restTemplate;
    private final LogService logService;



    public ModelAndView getPage(String query){
        logService.logInfo("getting a google page");
        String googleSearchUrl = "https://www.google.com/search?q=" + query;
        String htmlContent = restTemplate.getForObject(googleSearchUrl, String.class);

        logService.logInfo("creating a returned view");
        ModelAndView modelAndView = new ModelAndView("returnedPage");
        modelAndView.addObject("htmlContent", renaming(htmlContent));
        return modelAndView;
    }



    private String renaming(String htmlContent){
        logService.logInfo("parsing html content");
        htmlContent = htmlContent.replace("<span class=\"V6gwVd\">G</span>",
                "<span class=\"V6gwVd\">I</span>");
        htmlContent = htmlContent.replace("<span class=\"iWkuvd\">o</span>",
                "<span class=\"iWkuvd\">r</span>");
        htmlContent = htmlContent.replace("<span class=\"cDrQ7\">o</span>",
                "<span class=\"cDrQ7\">i</span>");
        htmlContent = htmlContent.replace("<span class=\"V6gwVd\">g</span>",
                "<span class=\"V6gwVd\">s</span>");
        htmlContent = htmlContent.replace("<span class=\"ntlR9\">l</span>",
                "<span class=\"ntlR9\">s</span>");
        htmlContent = htmlContent.replace("<span class=\"iWkuvd tJ3Myc\">e</span>",
                "<span class=\"iWkuvd tJ3Myc\">kl</span>");

        htmlContent = htmlContent.replaceAll("href=\"/url\\?q=(.*?)&amp;.*?\"",
                "href=\"$1\"");
        htmlContent = htmlContent.replaceAll("href=\"/\\?sa=X&amp;ved=.*?\"",
                "href=\"http://localhost:8080/index\"");
        return htmlContent;
    }
}
