package com.square.mail2;

public class Param {
	  private long id;
	  private String param;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getParam() {
	    return param;
	  }

	  public void setParam(String param) {
	    this.param = param;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return param;
	  }
	} 