package com.timetabling.client.test.pages.cathedra;

import java.util.List;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.timetabling.client.communication.base.Communicator;
import com.timetabling.client.communication.entities.CathedraProxy;
import com.timetabling.client.communication.requests.CathedraRequest;

public class CathedraPage extends Composite {

	private static CathedraPageUiBinder uiBinder = GWT
			.create(CathedraPageUiBinder.class);

	interface CathedraPageUiBinder extends UiBinder<Widget, CathedraPage> {
	}

	@UiField Button saveButton;
	@UiField TextBox nameSetter;
	@UiField TextBox emailSetter;
	@UiField FlowPanel persistedCathedras;

	public CathedraPage() {
		initWidget(uiBinder.createAndBindUi(this));
		updateList();
	}

	@UiHandler("saveButton")
	void onClick(ClickEvent e) {
		CathedraRequest requestContext = Communicator.get().requestFactory.createCathedraRequest();
		CathedraProxy cathedra = requestContext.create(CathedraProxy.class);
		cathedra.setName(nameSetter.getValue());
		cathedra.setEmail(emailSetter.getValue());
		requestContext.putCathedra(cathedra).
		fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				updateList();
			}
		});
	
	}
	
	private void updateList() {
		Communicator.get().requestFactory.createCathedraRequest().getAllCathedras().
		fire(new Receiver<List<CathedraProxy>>() {
			@Override
			public void onSuccess(List<CathedraProxy> response) {
				persistedCathedras.clear();
				for (CathedraProxy cathedra : response) {
					Label cathedraLabel = new Label();
					cathedraLabel.setText(cathedra.getId() + ": " + cathedra.getName() + " " + cathedra.getEmail());
					persistedCathedras.add(cathedraLabel);
				}
			}
		});
	}

}
