package com.timetabling.client.communication.requests;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.timetabling.client.communication.entities.TeacherProxy;
import com.timetabling.client.communication.entities.WishProxy;
import com.timetabling.server.base.data.dao.DaoServiceLocator;
import com.timetabling.server.data.managers.simple.TeacherManager;

@Service( value = TeacherManager.class, locator = DaoServiceLocator.class )
public interface TeacherRequest extends RequestContext {

	Request<Void> putTeacher(TeacherProxy teacher, long cathedraIds);
	Request<Void> putTeacher(TeacherProxy teacher);
	Request<Void> deleteTeacher(long teacherId);
	Request<List<TeacherProxy>> getAllTeachers();
	Request<Void> setTeacherName(long teacherId, String teacherName);
	Request<Void> setTeacherRank(long teacherId, int teacherRankCode);
	Request<Void> addWish(long teacherId, WishProxy wish);
	Request<List<WishProxy>> getAllWishesFor(long teacherId);
	Request<Void> deleteWish(long teacherId, long wishId);
	
	// TODO
	
}
