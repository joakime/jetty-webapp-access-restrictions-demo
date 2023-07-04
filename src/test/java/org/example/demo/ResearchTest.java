package org.example.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ResearchTest
{
    private Server server;

    @BeforeEach
    public void startServer() throws Exception
    {
        server = new Server();

        // HTTP/1.1 connector
        ServerConnector connector = new ServerConnector(server);
        server.addConnector(connector);

        // Run as webapp as project is <packaging>war</packaging>
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setResourceBase("src/main/webapp/");
        webAppContext.setExtraClasspath("target/classes");

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.addHandler(webAppContext);

        server.setHandler(contexts);
        server.start();
    }

    @AfterEach
    public void stopServer()
    {
        LifeCycle.stop(server);
    }

    @Test
    public void testPost() throws IOException
    {
        URI destURI = server.getURI().resolve("/demo/");
        HttpURLConnection httpURLConnection = (HttpURLConnection)destURI.toURL().openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write("This is from testPost()".getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(200, httpURLConnection.getResponseCode());
        String responseBody = IO.toString(httpURLConnection.getInputStream(), StandardCharsets.UTF_8);
        MatcherAssert.assertThat(responseBody, Matchers.containsString("got note from"));
    }
}
