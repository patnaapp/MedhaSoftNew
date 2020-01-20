package bih.nic.medhasoft.Model;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class checkboxitem implements KvmSerializable {
    private String id;
    private String stuientId;
    private String studentStats;
    private String EntryBy;
    private String collrefid;
    private String attendenceDate;
    private String session;
    private String totNoClasses;
    private String classAttended;
    private String latitude;
    private String longtude;
    private String groupPhoto;
    private String eduType;
    private String schoolName;

    @Override

    public Object getProperty(int index) {
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public void setProperty(int index, Object value) {

    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {

    }

    public String getStuientId() {
        return stuientId;
    }

    public void setStuientId(String stuientId) {
        this.stuientId = stuientId;
    }

    public String getStudentStats() {
        return studentStats;
    }

    public void setStudentStats(String studentStats) {
        this.studentStats = studentStats;
    }

    public String getEntryBy() {
        return EntryBy;
    }

    public void setEntryBy(String entryBy) {
        EntryBy = entryBy;
    }

    public String getCollrefid() {
        return collrefid;
    }

    public void setCollrefid(String collrefid) {
        this.collrefid = collrefid;
    }

    public String getAttendenceDate() {
        return attendenceDate;
    }

    public void setAttendenceDate(String attendenceDate) {
        this.attendenceDate = attendenceDate;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getTotNoClasses() {
        return totNoClasses;
    }

    public void setTotNoClasses(String totNoClasses) {
        this.totNoClasses = totNoClasses;
    }

    public String getClassAttended() {
        return classAttended;
    }

    public void setClassAttended(String classAttended) {
        this.classAttended = classAttended;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtude() {
        return longtude;
    }

    public void setLongtude(String longtude) {
        this.longtude = longtude;
    }

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public String getEduType() {
        return eduType;
    }

    public void setEduType(String eduType) {
        this.eduType = eduType;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
