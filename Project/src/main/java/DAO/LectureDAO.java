package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import VO.CourseVO;
import VO.LectureVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LectureDAO {

	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private DataSource dataSource;
	private CourseVO courseVO = null;
	private LectureVO lectureVO = null;
	public LectureDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void resourceRelease() {

		try {
			if (rs != null) {
				rs.close();
			}
			if (con != null) {
				con.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<LectureVO> getLecturesInfo(int courseId) {
		List<LectureVO> lectureList = new ArrayList<LectureVO>();
		try {
		con = dataSource.getConnection();
		String sql = "select * from lectures where course_Id = ?";
		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, courseId);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			lectureVO = new LectureVO();
			lectureVO.setCourseId(courseId);
			lectureVO.setDuration(rs.getInt("duration"));
			lectureVO.setLectureId(rs.getInt("lecture_id"));
			lectureVO.setLectureNumber(rs.getInt("lecture_number"));
			lectureVO.setLectureTitle(rs.getString("lecture_title"));
			lectureVO.setLectureSummary(rs.getString("lecture_summary"));
			lectureVO.setVideoLink(rs.getString("video_link"));
			lectureVO.setImgPath(rs.getString("img_path"));
			lectureList.add(lectureVO);
		}
		}catch (Exception e) {
			log.error("getLecturesInfo error : {}",e);
		}finally{
			resourceRelease();
		}
		return lectureList;
	}


	
	
	

}
