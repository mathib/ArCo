package it.cnr.istc.stlab.arco.web.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import it.cnr.istc.stlab.arco.Converter;

@Path("/")
public class RestApi {

	@GET
	@Produces("text/plain")
	public Response get(){
		return Response.ok("Hello, world!").build();
	}

	@POST
	@Produces({"text/turtle","application/rdf+xml", "application/n-triples", "text/n3", "application/json-ld"})
	public Response convert(@FormDataParam("file") InputStream xmlInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail){
		
		String fileName = fileDetail.getFileName();
		fileName = fileName.replace(".xml", "");
		Converter converter = new Converter();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		converter.convert(fileName, xmlInputStream, baos);
		
		byte[] baosBytes = baos.toByteArray();
		String content = new String(baosBytes);
		
		ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
		
		Model model = ModelFactory.createDefaultModel();
		model.read(in, null, "RDF/XML");
		
		return Response.ok(model).build();
		
		
	}
	
}
