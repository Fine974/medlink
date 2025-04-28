package org.example.medlink.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class PageController {

    @GetMapping("/drug-detail/{dbId}")
    public void redirectToDrugDetailPage(@PathVariable String dbId, HttpServletResponse response) throws IOException {
        if (dbId == null || dbId.trim().isEmpty() || "-".equals(dbId)) {
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/drug-detail.html?id=" + dbId);
        }
    }

    @GetMapping("/disease-detail/{omimId}")
    public void redirectToDiseaseDetailPage(@PathVariable String omimId, HttpServletResponse response) throws IOException {
        if (omimId == null || omimId.trim().isEmpty() || "-".equals(omimId)) {
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/disease-detail.html?id=" + omimId);
        }
    }
}
