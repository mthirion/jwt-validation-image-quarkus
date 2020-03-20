package com.redhat.appdev.auth;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;

import org.eclipse.microprofile.config.inject.ConfigProperty;
//import org.eclipse.microprofile.rest.client.RestClientBuilder;
//import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

import io.quarkus.security.Authenticated;

@Path("/")
public class JWTProxy {


	private Client client;
	
	@ConfigProperty(name = "proxy.target.port")
	private String targetPort;
	
	public JWTProxy() {
		client=ResteasyClientBuilderImpl.newClient();
	}
	
	@GET
	@Path("{var:.+}")
    @Produces(MediaType.TEXT_PLAIN)
	//@Authenticated
    public Response proxyGet(@Context HttpServletRequest request) {
    	
		String method = request.getMethod();
		String path = request.getServletPath();
		
		System.out.println("redirecting (" + path + ") to : localhost:" + targetPort + path + " [method=" + method + "]");
		return client.target("http://localhost:"+targetPort+path).request().get();
    }
	
	@POST
	@Path("{var:.+}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response proxyPost(@Context HttpServletRequest request, String body) {
    	
		String method = request.getMethod();
		String path = request.getServletPath();
		
		System.out.println("redirecting (" + path + ") to : localhost:" + targetPort + path + " [method=" + method + "]");
		return client.target("http://localhost:"+targetPort+path).request().post(Entity.json(body));
    }

	@PUT
	@Path("{var:.+}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response proxyPut(@Context HttpServletRequest request, String body) {
    	
		String method = request.getMethod();
		String path = request.getServletPath();
		
		System.out.println("redirecting (" + path + ") to : localhost:" + targetPort + path + " [method=" + method + "]");
		return client.target("http://localhost:"+targetPort+path).request().put(Entity.json(body));
    }	
	// we do not want to provide TRACE, OPTIONS, CONNECT, PATCH to access our applications
	
	
	@DELETE
	@Path("{var:.+}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response proxyDelete(@Context HttpServletRequest request) {
    	
		String method = request.getMethod();
		String path = request.getServletPath();
		
		System.out.println("redirecting (" + path + ") to : localhost:" + targetPort + path + " [method=" + method + "]");
		return client.target("http://localhost:"+targetPort+path).request().delete();
    }
		
}