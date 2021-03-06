package com.timetabling.server.data.entities.timetabling.lesson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Parent;
import com.timetabling.server.base.data.entities.DatastoreLongEntity;
import com.timetabling.server.data.entities.curriculum.CurriculumCell;
import com.timetabling.server.data.entities.curriculum.extentions.Teacher;
import com.timetabling.server.data.entities.timetabling.Time;
import com.timetabling.server.data.entities.timetabling.tt.GroupTT;
import com.timetabling.server.data.entities.timetabling.tt.TeacherTT;

@Entity
@Cached
public class Lesson extends DatastoreLongEntity {

	@Parent private Key<CurriculumCell> parent;
	private byte subGroupNumber;
	private boolean isFlushing;
	private Long teacherId = null;

	@Transient private Time time;
	@Transient private Map<Long, Time> versionTimeMap;
	// TODO problems counting
	@Transient private Map<Long, Integer> versionProblemsMap;
	@Transient private List<GroupTT> groupTTs = null;
	@Transient private TeacherTT teacherTT;
	@Transient private List<Lesson> lessonsWithMyTeacherOrGroup = null;		// = groupTTs(i).lessons + taecherTT.lessons
	@Transient private Teacher teacher;
	@Transient private CurriculumCell curriculumCell;

	public Lesson() {
	}
	
	public Lesson(Key<CurriculumCell> parent, byte subgroupNumber, boolean isFlushing) {
		this.parent = parent;
		this.subGroupNumber = subgroupNumber;
		this.isFlushing = isFlushing;
	}
	
	public byte getSubGroupNumber() {
		return subGroupNumber;
	}

	public void setSubGroupNumber(byte subGroupNumber) {
		this.subGroupNumber = subGroupNumber;
	}
	
	public boolean isFlushing() {
		return isFlushing;
	}

	public void setFlushing(boolean isFlushing) {
		this.isFlushing = isFlushing;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
	
	public List<Lesson> findPotentialCollisions(Time targetTime, Long version) {
		List<Lesson> collisions=null;
		for (Lesson lesson : lessonsWithMyTeacherOrGroup)
			if (lesson != this && lesson.getTime().hasConflictWith(targetTime)){
				if (collisions == null)
					collisions = new LinkedList<Lesson>();
				collisions.add(lesson);
			}
		return collisions;
	}

	public int countPotentialCollisions(Time targetTime, Long version) {
		int countOfCollisions = 0;
		for (Lesson lesson : lessonsWithMyTeacherOrGroup)
			if (lesson != this && lesson.getTime().hasConflictWith(targetTime))
				countOfCollisions++;
		return countOfCollisions;
	}

	public boolean hasPotentialCollisions(Time targetTime) {
		for (Lesson lesson : lessonsWithMyTeacherOrGroup)
			if (lesson != this && lesson.getTime().hasConflictWith(targetTime))
				return true;
		return false;
	}

	public boolean hasCollisions() {
		for (Lesson lesson : lessonsWithMyTeacherOrGroup)
			if (lesson != this && lesson.getTime().hasConflictWith(time))
				return true;
		return false;
	}
	
	public List<GroupTT> getGroupTTs() {
		return groupTTs;
	}
	
	void addGoupTT(GroupTT groutTT) {
		if (groupTTs == null)
			groupTTs = new ArrayList<GroupTT>(curriculumCell.getNumberOfSubgroups());
		groupTTs.add(groutTT);
	}

	public Time getTime() {
		return time;
	}
	
	public void setTime(Time time) {
		this.time = time;
	}

	public Key<CurriculumCell> getParent() {
		return parent;
	}

	public void setParent(Key<CurriculumCell> parent) {
		this.parent = parent;
	}
	
	public Map<Long, Time> getVersionTimeMap() {
		return versionTimeMap;
	}
	
	public void setVersionTimeMap(Map<Long, Time> versionTimeMap) {
		this.versionTimeMap = versionTimeMap;
	}

	public Time getTimeInVersion(Long version) {
		return versionTimeMap.get(version);
	}

	public void setTimeInVersion(Time time, Long version) {
		versionTimeMap.put(version, time);
	}
	
	public void setThisTimeForVersion(Long version) {
		versionTimeMap.put(version, time);
	}
	
	public void setTimeFromVersion(Long version) {
		time = getTimeInVersion(version);
	}
	
	public void setTimeFromVersionAndMoveLessonsInTTs(Long version) {
		time = getTimeInVersion(version);
		teacherTT.addLesson(this);
		for (GroupTT groupTT : groupTTs)
			groupTT.addLesson(this);
	}
	
	public void removeVersion(Long version) {
		versionTimeMap.remove(version);
	//	TODO 
	//	versionProblemsMap.remove(version);
	}

	public Map<Long, Integer> getVersionProblemsMap() {
		return versionProblemsMap;
	}

	public void setVersionProblemsMap(Map<Long, Integer> versionProblemsMap) {
		this.versionProblemsMap = versionProblemsMap;
	}
	
	public void incrementProblem(Long version) {
		Integer problems = versionProblemsMap.get(version);
		if (problems == null)
			versionProblemsMap.put(version, 0);
		versionProblemsMap.put(version, problems+1);
	}
	
	public void resetProblems(Long version) {
		versionProblemsMap.put(version, 0);
	}

	public TeacherTT getTeacherTT() {
		return teacherTT;
	}

	public void setTeacherTT(TeacherTT teacherTT) {
		this.teacherTT = teacherTT;
	}

	public List<Lesson> getLessonsWithMyTeacherOrGroup() {
		return lessonsWithMyTeacherOrGroup;
	}

	public void setLessonsWithMyTeacherOrGroup(
			List<Lesson> lessonsWithMyTeacherOrGroup) {
		this.lessonsWithMyTeacherOrGroup = lessonsWithMyTeacherOrGroup;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public CurriculumCell getCurriculumCell() {
		return curriculumCell;
	}

	public void setCurriculumCell(CurriculumCell curriculumCell) {
		this.curriculumCell = curriculumCell;
	}

	public void setGroupTTs(List<GroupTT> groupTTs) {
		this.groupTTs = groupTTs;
	}
	
	@Override
	public String toString() {
		return "Les_" + getId();
	}
	
}
