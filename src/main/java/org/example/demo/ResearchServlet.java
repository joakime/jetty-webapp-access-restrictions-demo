package org.example.demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jetty.xml.XmlParser;
import org.eclipse.jetty.xml.XmlParser.Node;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResearchServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String xmlContent = read(req);
        XmlParser xmlParser = new XmlParser(true);
        InputStream targetStream = new ByteArrayInputStream(xmlContent.getBytes());
        boolean has_error = true;
        try {
            Node node = xmlParser.parse(targetStream);
            String from = node.get("from").toString();
            write(resp, "got note from: " + from);
            System.out.println(node.toString());
            has_error = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (has_error) {
            write(resp, "error while parsing xml");
        }
    }

    private void write(HttpServletResponse resp, String message) throws IOException {
        resp.getWriter().write(message);
    }

    private String read(HttpServletRequest req) throws IOException {
        String data = req.getReader().readLine();
        return data;
    }
}