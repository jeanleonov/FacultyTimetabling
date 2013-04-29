package com.timetabling.server.data.managers.simple;

import java.lang.reflect.Method;
import java.util.List;

import com.timetabling.server.base.common.KeyHelper;
import com.timetabling.server.base.common.NamespaceController;
import com.timetabling.server.base.data.dao.DAOT;
import com.timetabling.server.base.data.dao.DAOT.DatastoreOperation;
import com.timetabling.server.base.data.dao.GenericDAO;
import com.timetabling.server.base.data.dao.ObjectifyDao;
import com.timetabling.server.data.entities.curriculum.Subject;

public class SubjectManager extends GenericDAO<Subject> {

	public SubjectManager() {
		super(Subject.class);
	}
	
	public void putSubject(Subject subject) throws Exception {
		// TODO see cathedra manager
		Utils.setNamespaceGeneral();
		put(subject);
	}
	
	public void deleteSubject(final long subjectId) throws Exception {
		Utils.setNamespaceGeneral();
		DAOT.runInTransaction(logger, new DatastoreOperation<Void>() {
			@Override
			public Void run(DAOT daot) throws Exception {
				daot.getOfy().delete(Subject.class, subjectId);
				return null;
			}
			@Override
			public String getOperationName() {
				return "Deleting of subject.";
			}
		});
	}
	
	/** This method try to find subject with specified name. <br>
	 *  If it found subject -> return id of founded subject, <br> 
	 *  otherwise -> create and persist new subject with specified name, and return id of created entity */
	public long getSubjectIdFor(final String subjectName) throws Exception {
		Utils.setNamespaceGeneral();
		final ObjectifyDao<Subject> dao = new ObjectifyDao<Subject>(Subject.class);
		return DAOT.runInTransaction(logger, new DatastoreOperation<Long>() {
			@Override
			public Long run(DAOT daot) throws Exception {
				Subject subject = dao.getByProperty("name", subjectName);
				if (subject != null)
					return subject.getId();
				Subject newSubject = new Subject();
				newSubject.setName(subjectName);
				newSubject.setDisplayName(subjectName);
				return daot.getOfy().put(newSubject).getId();
			}
			@Override
			public String getOperationName() {
				return "Saving of new subject";
		}});
	}
	
	public Subject getSubjectById(long id) throws Exception {
		Utils.setNamespaceGeneral();
		return get(KeyHelper.getKey(Subject.class, id));
	}
	
	public List<Subject> getAllSubjects() {
		Utils.setNamespaceGeneral();
		return ofy().query(Subject.class).list();
	}
	
	public void setSubjectName(long subjectId, String subjectName) throws Exception {
		Method nameSetter = Subject.class.getMethod("setName", String.class);
		Utils.<Subject>setFieldValueInEntity(
				NamespaceController.generalNamespace, Subject.class, subjectId, subjectName, nameSetter);
	}
	
	public void setSubjectDisplayName(long subjectId, String subectDisplayName) throws Exception {
		Method nameSetter = Subject.class.getMethod("setDisplayName", String.class);
		Utils.<Subject>setFieldValueInEntity(
				NamespaceController.generalNamespace, Subject.class, subjectId, subectDisplayName, nameSetter);
	}

}
