package com.example.kl.home.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Student extends StudentId implements Serializable , Parcelable {
	public Student() {

	}
	ArrayList<String> class_id=new ArrayList<>();
	String student_department;
	String student_email;
	String student_id;
	String student_name;
	String student_school;
	String image_url;

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public ArrayList<String> getClass_id() {
		return class_id;
	}
	public void setClass_id(ArrayList<String> class_id) {
		this.class_id = class_id;
	}
	public String getStudent_department() {
		return student_department;
	}
	public void setStudent_department(String student_department) {
		this.student_department = student_department;
	}
	public String getStudent_email() {
		return student_email;
	}
	public void setStudent_email(String student_email) {
		this.student_email = student_email;
	}
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	public String getStudent_school() {
		return student_school;
	}
	public void setStudent_school(String student_school) {
		this.student_school = student_school;
	}
	protected Student(Parcel in) {
		class_id = (ArrayList<String>) in.readSerializable();
		student_department = in.readString();
		student_email = in.readString();
		student_id = in.readString();
		student_name = in.readString();
		student_school = in.readString();
		image_url = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// 寫入參數，參數順序要和建構子一樣
	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeSerializable(class_id);
		parcel.writeString(student_department);
		parcel.writeString(student_email);
		parcel.writeString(student_id);
		parcel.writeString(student_name);
		parcel.writeString(student_school);
		parcel.writeString(image_url);
	}

	public static final Creator<Student> CREATOR = new Creator<Student>() {
		@Override
		public Student createFromParcel(Parcel in) {
			return new Student(in);
		}

		@Override
		public Student[] newArray(int size) {
			return new Student[size];
		}
	};


}