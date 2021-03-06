package com.sannong.presentation.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sannong.domain.valuetypes.ResponseStatus;
import com.sannong.infrastructure.util.PasswordGenerator;
import com.sannong.presentation.model.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sannong.infrastructure.dataexport.CsvExporter;
import com.sannong.domain.entities.Answer;
import com.sannong.domain.entities.SMS;
import com.sannong.domain.entities.User;
import com.sannong.presentation.model.DTO;
import com.sannong.service.IProjectService;
import com.sannong.service.ISmsService;
import com.sannong.service.IUserService;

@Controller
public class PersonalCenterController {
    private static final Logger logger = Logger.getLogger(PersonalCenterController.class);
    private static final long pageSum = 10;

    private static final String PAGE_USER_APPLICATION_FORM = "user-application-form";
    private static final String PAGE_USER_PROFILE = "user-profile";
    private static final String PAGE_USER_PASSWORD = "user-password";
    private static final String PAGE_USER_MANAGEMENT = "user-management";
    private static final String USER_PERSONAL_CENTER_PAGE = "user-personal-center";

    @Resource
    private IUserService userService;
    @Resource
    private ISmsService smsService;
    @Resource
    private IProjectService projectService;


    @RequestMapping(value = "user-personal-center", method = RequestMethod.GET)
    public ModelAndView showUserPersonalCenter() {

        Map<String, Object> models = new HashMap<String, Object>();
        models.put("user-personal-center", new Object());
        return new ModelAndView(USER_PERSONAL_CENTER_PAGE, models);
    }

    @RequestMapping(value = {"user-personal-center/user-profile"}, method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> userProfile(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        if (userName == null){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
            } else {
                userName = principal.toString();
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", userName);
        List<User> users = userService.getUserByCondition(map);

        Map<String, Object> models = new HashMap<String, Object>();
        models.put("userProfile", users.get(0));

        return models;
    }

    @RequestMapping(value = "user-application-form", method = RequestMethod.GET)
    public ModelAndView showUserApplicationForm() {
        Map<String, Object> models = new HashMap<String, Object>();
        models.put("user-application-form", new Object());
        return new ModelAndView(PAGE_USER_APPLICATION_FORM, models);
    }

    @RequestMapping(value = "user-profile", method = RequestMethod.GET)
    public ModelAndView showUserProfile() {
        Map<String, Object> models = new HashMap<String, Object>();
        models.put("user-profile", new Object());
        return new ModelAndView(PAGE_USER_PROFILE, models);
    }

    @RequestMapping(value = "user-management", method = RequestMethod.GET)
    public ModelAndView showUserManagement() {
        Map<String, Object> models = new HashMap<String, Object>();
        models.put("user-management", new Object());
        return new ModelAndView(PAGE_USER_MANAGEMENT, models);
    }

    @RequestMapping(value = "user-password", method = RequestMethod.GET)
    public ModelAndView showUserPassword() {
        Map<String, Object> models = new HashMap<String, Object>();
        models.put("user-password", new Object());
        return new ModelAndView(PAGE_USER_PASSWORD, models);
    }

    /********************************
     * Show applicants page
     * @param request
     * @return
     * @throws Exception
     ********************************/
    @RequestMapping(value = "showApplicants", method = RequestMethod.GET)
    public @ResponseBody List<User> showList(HttpServletRequest request) throws Exception {

        Map<String, Object> map = new HashMap<String,Object>();
        String pageIndex = request.getParameter("pageIndex");
        String cellphone = request.getParameter("cellphone");
    	String realName = request.getParameter("realName");
    	String company = request.getParameter("company");
    	String jobTitle = request.getParameter("jobTitle");
    	String companyAddress = request.getParameter("companyAddress");
    	String mailbox = request.getParameter("mailbox");
    	String provinceIndex = request.getParameter("provinceIndex");
    	String cityIndex = request.getParameter("cityIndex");
    	String districtIndex = request.getParameter("districtIndex");

        int pageStart = 0;

        if (null != pageIndex){
            pageStart =  (Integer.parseInt(pageIndex)- 1)  * 10;
        }
        map.put("pageStart", pageStart);
        map.put("pageSum", pageSum);
        map.put("cellphone", cellphone);
        map.put("realName", realName);
        map.put("company", company);
    	map.put("jobTitle", jobTitle);
    	map.put("companyAddress", companyAddress);
    	map.put("mailbox", mailbox);
    	map.put("companyProvince", provinceIndex);
    	map.put("companyCity", cityIndex);
    	map.put("companyDistrict", districtIndex);

        List<User> applicants = userService.getUserByFuzzyMatch(map);

        return applicants;
    }

    /************************************
     * Update user's information.
     * @param request
     * @param user
     * @return
     ************************************/
    @RequestMapping(value = {"updateUserProfile"}, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateUserProfile(HttpServletRequest request, @ModelAttribute("userProfile") User user) {
        String newCellphone = request.getParameter("newCellphone");
        String validationCode = request.getParameter("validationCode");

        Map<String, Object> models = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(newCellphone) && StringUtils.isNotBlank(validationCode)){
            List<SMS> smsList = smsService.getSmsByCellphoneAndValidationCode(newCellphone, validationCode);
            if (smsList.isEmpty()){
                models.put("status", "error");
                models.put("userProfile", user);
                return models;
            }else{
                user.setCellphone(newCellphone);
            }
        }

    	Timestamp ts = new Timestamp(System.currentTimeMillis());
		user.setUpdateTime(ts);
		try {
			userService.updateUser(user);
		} catch (Exception e) {
			logger.error(e.getMessage());
            models.put("status", "error");
		}
		models.put("userProfile", user);
		models.put("status", "saved");
        return models;
	}

    /**************************************
     * Get user total count.
     * @param request
     * @return
     * @throws Exception
     **************************************/
    @RequestMapping(value = "userTotalCount", method = RequestMethod.GET)
    public @ResponseBody String getUserTotalCount(HttpServletRequest request) throws Exception {

    	Map<String, Object> map = new HashMap<String,Object>();
    	String cellphone = request.getParameter("cellphone");
    	String realName = request.getParameter("realName");
    	String company = request.getParameter("company");
    	String jobTitle = request.getParameter("jobTitle");
    	String companyAddress = request.getParameter("companyAddress");
    	String mailbox = request.getParameter("mailbox");
    	String provinceIndex = request.getParameter("provinceIndex");
    	String cityIndex = request.getParameter("cityIndex");
    	String districtIndex = request.getParameter("districtIndex");

    	map.put("cellphone", cellphone);
    	map.put("realName", realName);
    	map.put("company", company);
    	map.put("jobTitle", jobTitle);
    	map.put("companyAddress", companyAddress);
    	map.put("mailbox", mailbox);
    	map.put("companyProvince", provinceIndex);
    	map.put("companyCity", cityIndex);
    	map.put("companyDistrict", districtIndex);

    	return userService.getUserTotalCount(map);
    }

    /*************************************
     * Update user's password.
     * @param request
     * @return
     * @throws Exception
     **************************************/
    @RequestMapping(value = "updatePassword", method = RequestMethod.POST)
    public @ResponseBody
    Response updatePassword(HttpServletRequest request) throws Exception {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmedPassword = request.getParameter("confirmedPassword");
        String userName = request.getParameter("userName");

        if (StringUtils.isBlank(userName)){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
            } else {
                userName = principal.toString();
            }
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userName", userName);
        List<User> users = userService.getUserByCondition(map);
        if ( ! (users.isEmpty()) ){
            User user = users.get(0);
            String encryptOldPassword = PasswordGenerator.encryptPassword(oldPassword, userName);
            if ( ! (user.getPassword().equals(encryptOldPassword))){
                return new Response(
                        ResponseStatus.OLD_PASSWORD_MISMATCH.getStatusCode(),
                        ResponseStatus.OLD_PASSWORD_MISMATCH.getStatusDescription());
            }else if ( ! (newPassword.equals(confirmedPassword)) ){
                return new Response(
                        ResponseStatus.CONFIRMED_PASSWORD_MISMATCH.getStatusCode(),
                        ResponseStatus.CONFIRMED_PASSWORD_MISMATCH.getStatusDescription());
            }
            String encryptedNewPassword = PasswordGenerator.encryptPassword(newPassword, userName);
            user.setPassword(encryptedNewPassword);
            userService.updatePassword(user);

            return new Response(
                    ResponseStatus.PASSWORD_UPDATED.getStatusCode(),
                    ResponseStatus.PASSWORD_UPDATED.getStatusDescription());
        }else{
            return new Response(
                    ResponseStatus.USER_NOT_FOUND.getStatusCode(),
                    ResponseStatus.USER_NOT_FOUND.getStatusDescription());
        }
    }

    /************************************************
     * Update answers from questionnaire page.
     * @param answer
     * @return
     * @throws Exception
     ************************************************/
    @RequestMapping(value = "updateAnswersAndComment", method = RequestMethod.POST)
    public @ResponseBody DTO updateAnswersAndComment(@ModelAttribute("answerForm") Answer answer) throws Exception{

    	String userName = null;
        Boolean result = true;

    	if (answer.getAnswers() == null){
    		result = false;
    		return new DTO(result,null);
    	}

        if (answer.getApplicant() != null){
        	userName = answer.getApplicant().getUserName();
        }else {
        	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        	if (principal instanceof UserDetails) {
        		SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            	userName = ((UserDetails) principal).getUsername();
        	}else{
        		SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            	userName = principal.toString();
        	}
        }

        User applicant = new User();
        applicant.setUserName(userName);
        answer.setApplicant(applicant);
        result = projectService.updateAnswersAndComment(answer);

        return new DTO(result,null);
    }

    /**************************************
     * Export answers to CSV file.
     * @param request
     * @return
     * @throws Exception
     ***************************************/
    @RequestMapping(value = "exportCSV", method = RequestMethod.POST)
    public @ResponseBody DTO exportAll(HttpServletRequest request) throws Exception {

        Map<String, Object> map = new HashMap<String,Object>();
        String cellphone = request.getParameter("cellphone");
    	String realName = request.getParameter("realName");
    	String company = request.getParameter("company");
    	String jobTitle = request.getParameter("jobTitle");
    	String companyAddress = request.getParameter("companyAddress");
        String mailbox = request.getParameter("mailbox");
        String provinceIndex = request.getParameter("provinceIndex");
        String cityIndex = request.getParameter("cityIndex");
        String districtIndex = request.getParameter("districtIndex");

        map.put("pageStart", 0);
        map.put("cellphone", cellphone);
        map.put("realName", realName);
        map.put("company", company);
        map.put("jobTitle", jobTitle);
        map.put("companyAddress", companyAddress);
        map.put("mailbox", mailbox);
        map.put("companyProvince", provinceIndex);
        map.put("companyCity", cityIndex);
        map.put("companyDistrict", districtIndex);

        String pageSum = userService.getUserTotalCount(map);
        map.put("pageSum", Integer.parseInt(pageSum));

        List<User> applicants = userService.getUserByFuzzyMatch(map);
        int questionSum = projectService.getTotalQuestions();
        String filePath = CsvExporter.export(applicants,questionSum);

        String[] filePathSplit = filePath.split("/");
        String fileName = filePathSplit[3];

        return new DTO(true,fileName);
    }


    @RequestMapping(value = "downloadCsv", method = RequestMethod.GET)
    public void downloadCsv(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String fileName = request.getParameter("csvFileName");

        java.io.BufferedInputStream bufferInputStream = null;
        java.io.BufferedOutputStream bufferOutputStream = null;

        String ctxPath = request.getSession().getServletContext().getRealPath("/") + "csvfiles/";
        String downLoadPath = ctxPath + fileName;

        try {
            long fileLength = new File(downLoadPath).length();

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));

            bufferInputStream = new BufferedInputStream(new FileInputStream(downLoadPath));
            bufferOutputStream = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bufferInputStream.read(buff, 0, buff.length))) {
                bufferOutputStream.write(buff, 0, bytesRead);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (bufferInputStream != null){
                bufferInputStream.close();
            }
            if (bufferOutputStream != null){
                bufferOutputStream.close();
            }
        }
    }
}
