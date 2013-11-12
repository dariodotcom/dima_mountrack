package it.polimi.dima.dacc.mountainroute.commons.types;

public class RouteDescription {

	private String name;
	private String id;

	public RouteDescription(String id, String name) {
		super();
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		String format = "{\"id\":\"%s\",\"name\"=\"%s\"}";
		return String.format(format, this.id, this.name);
	}

}
