package mypkg;

import jakarta.batch.api.BatchProperty;
import jakarta.enterprise.util.AnnotationLiteral;

public class BatchPropertyLiteral extends AnnotationLiteral<BatchProperty> implements BatchProperty {

	private String name;

	public BatchPropertyLiteral() {
		this("ddd");
	}

	public BatchPropertyLiteral(String name) {
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

}
