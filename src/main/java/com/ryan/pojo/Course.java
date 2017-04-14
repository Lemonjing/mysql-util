package com.ryan.pojo;

/**
 * @author Ryan Tao
 * @github lemonjing
 */
public class Course {
    /**
     * 课程号 主键
     */
    private String courseId;

    /**
     * 申请课程日期
     */
    private String applyDate;

    /**
     * 审核日期
     */
    private String approveDate;

    /**
     * 课程描述
     */
    private String courseIntro;

    /**
     * 课程状态
     */
    private String courseState;

    /**
     * 课程标题
     */
    private String courseTitle;

    /**
     * 课程浏览量
     */
    private int scanNum;

    /**
     * 分类
     */
    private String catagory;

    /**
     * 评分
     */
    private int grade;

    /**
     * 热度
     */
    private int hot;

    /**
     * 文件路径
     */
    private String videoPath;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getCourseIntro() {
        return courseIntro;
    }

    public void setCourseIntro(String courseIntro) {
        this.courseIntro = courseIntro;
    }

    public String getCourseState() {
        return courseState;
    }

    public void setCourseState(String courseState) {
        this.courseState = courseState;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getScanNum() {
        return scanNum;
    }

    public void setScanNum(int scanNum) {
        this.scanNum = scanNum;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", applyDate='" + applyDate + '\'' +
                ", approveDate='" + approveDate + '\'' +
                ", courseIntro='" + courseIntro + '\'' +
                ", courseState='" + courseState + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                ", scanNum=" + scanNum +
                ", catagory='" + catagory + '\'' +
                ", grade=" + grade +
                ", hot=" + hot +
                ", videoPath='" + videoPath + '\'' +
                '}';
    }
}
