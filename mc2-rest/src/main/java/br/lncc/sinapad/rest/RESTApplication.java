package br.lncc.sinapad.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import br.lncc.sinapad.rest.file.FileRESTOperation;

@ApplicationPath("/")
public class RESTApplication extends ResourceConfig {

	public RESTApplication() {
		super(FileRESTOperation.class, MultiPartFeature.class);
	}
}