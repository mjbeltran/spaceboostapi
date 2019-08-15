package com.mbg.spaceboostapi.exception;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("errors")
public class ListErrors {
	private List<AttributeError> attributeErrors;

	public ListErrors(List<AttributeError> attributeErrors) {
		this.attributeErrors = attributeErrors;
	}

	public List<AttributeError> getAttributeErrors() {
		return attributeErrors;
	}

	public void setAttributeErrors(List<AttributeError> attributeErrors) {
		this.attributeErrors = attributeErrors;
	}
}