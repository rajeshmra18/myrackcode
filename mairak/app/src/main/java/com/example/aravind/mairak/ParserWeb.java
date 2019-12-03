package com.example.aravind.mairak;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ParserWeb {

//////Normal LoginActivity

    static String loginstatuserror;
    static String loginrecordno;
    static String loginresponse;
    static int loginarray;

    static String[] loginuserid;
    static String[] loginfirstname;
    static String[] loginlastname;
    static String[] loginemail;
    static String[] loginmobilenumber;
    static String[] notfyvideo;
    static String[] notifyarticle;
    static String[] notifydics;


    public static String Login(String data) throws Exception {
        try {
            JSONObject cobj = new JSONObject(data);
            loginresponse = cobj.getString("response");
            if (loginresponse.equals("Error")) {
                loginstatuserror = cobj.getString("response_desc");
            }
            loginrecordno = cobj.getString("numberOfRecords");

            JSONArray jaArray = cobj.optJSONArray("records");
            loginarray = jaArray.length();

            loginuserid = new String[loginarray];
            loginfirstname = new String[loginarray];

            loginlastname = new String[loginarray];
            loginemail = new String[loginarray];
            loginmobilenumber = new String[loginarray];

            notfyvideo = new String[loginarray];
            notifyarticle = new String[loginarray];
            notifydics = new String[loginarray];
            for (int j = 0; j < loginarray; j++) {
                JSONObject kobj = jaArray.getJSONObject(j);
                loginuserid[j] = kobj.getString("user_id");
                loginfirstname[j] = kobj.getString("first_name");
                loginlastname[j] = kobj.getString("last_name");
                loginemail[j] = kobj.getString("email");
                loginmobilenumber[j] = kobj.getString("mobile");
                notfyvideo[j] = kobj.getString("notify_video");
                notifyarticle[j] = kobj.getString("notify_article");
                notifydics[j] = kobj.getString("notify_forum");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginresponse;
    }

    public static String getloginresponse() {
        return loginresponse;
    }

    public static String getloginrecordno() {
        return loginrecordno;
    }

    public static int getloginarray() {
        return loginarray;
    }

    public static String[] loginuserid() {
        return loginuserid;
    }

    public static String[] loginfirstname() {
        return loginfirstname;
    }

    public static String[] loginlastname() {
        return loginlastname;
    }

    public static String[] loginmobilenumber() {
        return loginmobilenumber;
    }

    public static String[] notfyvideo() {
        return notfyvideo;
    }

    public static String[] notifyarticle() {
        return notifyarticle;
    }

    public static String[] notifydics() {
        return notifydics;
    }

    public static String[] email() {
        return loginemail;
    }

    public static String geterrorforlogin() {
        return loginstatuserror;
    }


    ///google places response

    static double Latitude;
    static double Longitude;
    static String g1;
    static String g2;
    static String g3;


    public static String googleplaces(String responseReg) {
        try {
            JSONObject cobj = new JSONObject(responseReg);

            g1 = cobj.getString("result");
            JSONObject att1 = new JSONObject(g1);
            g2 = att1.getString("geometry");
            JSONObject att2 = new JSONObject(g2);
            g3 = att2.getString("location");
            JSONObject att3 = new JSONObject(g3);
            Latitude = att3.getDouble("lat");
            Longitude = att3.getDouble("lng");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return g1;
    }

    public static double getlatitudefromgoogle() {
        return Latitude;
    }

    public static double getlongitudefromgoogle() {
        return Longitude;
    }


    //Symptom-Specialization
    static int specilizationnarraylength;
    static int symptomarraylength;
    static int symptomnewarraylength;
    static String specializationresp;
    static String specializationreecordsno;
    static String[] specializationid;
    static String[] specializationname;
    static String symptomreecordsno;
    static String[] symptomid;
    static String[] symptmname;
    ArrayList<Integer> selectedvaluespositin = new ArrayList<Integer>();

    public static String specialization(String data) throws Exception {
        try {
            JSONObject cobj = new JSONObject(data);
            specializationresp = cobj.getString("response");
            specializationreecordsno = cobj.getString("specialization_numOfRecords");
            symptomreecordsno = cobj.getString("symptom_numOfRecords");

            JSONArray jaArray = cobj.optJSONArray("specialization_records");
            specilizationnarraylength = jaArray.length();
            JSONArray jaArray1 = cobj.optJSONArray("symptom_records");
            symptomarraylength = jaArray1.length();

            specializationid = new String[specilizationnarraylength];
            specializationname = new String[specilizationnarraylength];
           String[] symptomid = new String[symptomarraylength];

            for (int j = 0; j < specilizationnarraylength; j++) {
                JSONObject kobj = jaArray.getJSONObject(j);
                specializationid[j] = kobj.getString("specialization_id");
                specializationname[j] = kobj.getString("specialization_name");

            }
            //    ArrayList<String>sympname=new ArrayList<String>();
            ArrayList<String> sympname = new ArrayList<String>();
            for (int i = 0,j=0; i < symptomarraylength; i++) {
                JSONObject kobj = jaArray1.getJSONObject(i);


                if (!sympname.contains(kobj.getString("symptom_name"))) {
                    sympname.add(kobj.getString("symptom_name"));
                    symptomid[j] = kobj.getString("symptom_id");
                    j++;

                }


            }
            symptomnewarraylength = sympname.size();
            symptmname = new String[symptomnewarraylength];
            symptmname=sympname.toArray(symptmname);
            ParserWeb.symptomid=new String[symptomnewarraylength];
            System.arraycopy(symptomid,0, ParserWeb.symptomid,0,symptomnewarraylength);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return specializationresp;
    }

    public static int specializationarray() {
        return specilizationnarraylength;
    }

    public static int symptomarray() {
        return symptomnewarraylength;
    }


    public static String[] getspecializationid() {
        return specializationid;
    }

    public static String[] getspecializationname() {
        return specializationname;
    }

    public static String[] getsymptomid() {
        return symptomid;
    }

    public static String[] getsymptomname() {
        return symptmname;
    }

    public static String getsymrecord() {
        return symptomreecordsno;
    }

    public static String getspecrecordno() {
        return specializationreecordsno;
    }


    //Get Video
    static int videoarraylength;
    static String videoresp;
    static String videoreecordsno;
    static String[] videoid;
    static String[] videoddesc;
    static String[] videouri;
    static String[] videothumb;
    static String[] videolike;
    static String[] videouserlike;
    static String[] videotitle;


    public static String getvideo(String responsevideo) {

        try {
            JSONObject cobj = new JSONObject(responsevideo);
            videoresp = cobj.getString("response");
            videoreecordsno = cobj.getString("numberOfRecords");

            JSONArray jaArray = cobj.optJSONArray("records");
            videoarraylength = jaArray.length();

            videoid = new String[videoarraylength];
            videoddesc = new String[videoarraylength];

            videouri = new String[videoarraylength];
            videothumb = new String[videoarraylength];
            videolike = new String[videoarraylength];
            videouserlike = new String[videoarraylength];
            videotitle = new String[videoarraylength];
            for (int j = 0; j < videoarraylength; j++) {
                JSONObject kobj = jaArray.getJSONObject(j);
                videoid[j] = kobj.getString("video_id");
                videoddesc[j] = kobj.getString("description");
                videouri[j] = kobj.getString("video_uri");
                videothumb[j] = kobj.getString("video_thumbnail");
                videolike[j] = kobj.getString("likes");
                videouserlike[j] = kobj.getString("userlike");
                videotitle[j] = kobj.getString("video_title");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoresp;
    }

    public static int getvideoarraylength() {
        return videoarraylength;
    }


    public static String[] getvideoidid() {
        return videoid;
    }

    public static String[] getvideodescription() {
        return videoddesc;
    }

    public static String[] getvideouri() {
        return videouri;
    }

    public static String[] getvideothumb() {
        return videothumb;
    }

    public static String[] videolike() {
        return videolike;
    }

    public static String[] videouserlike() {
        return videouserlike;
    }

    public static String[] videotitle() {
        return videotitle;
    }


    //Get Article
    static int artarraylength;
    static String artresp;
    static String artreecordsno;
    static String[] artid;
    static String[] artdesc;
    static String[] arttitle;
    static String[] artdatei;
    static String[] artimage;
    static String[] artlike;
    static String[] articlepdflink;
    static String[] userlikie;

    public static String getarticle(String responsarticle) {
        try {
            JSONObject cobj = new JSONObject(responsarticle);
            artresp = cobj.getString("response");
            artreecordsno = cobj.getString("numberOfRecords");

            JSONArray jaArray = cobj.optJSONArray("records");
            artarraylength = jaArray.length();

            artid = new String[artarraylength];
            artdesc = new String[artarraylength];
            arttitle = new String[artarraylength];
            artdatei = new String[artarraylength];
            artimage = new String[artarraylength];
            artlike = new String[artarraylength];
            articlepdflink = new String[artarraylength];
            userlikie = new String[artarraylength];


            for (int j = 0; j < artarraylength; j++) {
                JSONObject kobj = jaArray.getJSONObject(j);
                artid[j] = kobj.getString("article_id");
                artdesc[j] = kobj.getString("description");
                artdatei[j] = kobj.getString("date_created");
                artlike[j] = kobj.getString("likes");
                artimage[j] = kobj.getString("image");
                arttitle[j] = kobj.getString("title");
                userlikie[j] = kobj.getString("userlike");

                if (kobj.has("pdf_url") && !kobj.getString("pdf_url").equals("")) {
                    articlepdflink[j] = kobj.getString("pdf_url");
                } else {
                    articlepdflink[j] = "";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return artresp;
    }

    public static int getarticlearraylength() {
        return artarraylength;
    }

    public static String[] getarticleid() {
        return artid;
    }

    public static String[] getarticledescription() {
        return artdesc;
    }

    public static String[] getartimage() {
        return artimage;
    }

    public static String[] getartdate() {
        return artdatei;
    }

    public static String[] getartlike() {
        return artlike;
    }

    public static String[] getarttitle() {
        return arttitle;
    }

    public static String[] getuserlikie() {
        return userlikie;
    }

    public static String[] getpdflink() {
        return articlepdflink;
    }


    ///Submit Post
    static String postresp;
    static String postid;
    static String errormessage;

    public static String submitpost(String responsepost) {
        try {
            JSONObject cobj = new JSONObject(responsepost);

            postresp = cobj.getString("response");
            if (postresp.equals("Error")) {
                errormessage = cobj.getString("response_desc");
            } else {
                postid = cobj.getString("postID");

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return postresp;
    }

    public static String getpost() {
        return postresp;
    }

    public static String getpostid() {
        return postid;
    }

    public static String geterror() {
        return errormessage;
    }


    //List Post
    static int postlistarraylength;
    public static int postListArrayLengthOriginal;
    static String postlistresp;
    static String postlistreecordsno;
    static String[] postlistid;
    static String[] postlistcontnt;
    static String[] postlistuserid;
    static String[] postlistdatei;
    static String[] postlistactive;
    static String[] postcomment;
    static String[] postusername;
    static String[] postuserpic;
    static String[] posttitle;

    public static String listpost(String responsepost) {
        try {
            JSONObject cobj = new JSONObject(responsepost);
            postlistresp = cobj.getString("response");
            if (postlistresp.equals("Error")) {
                errormessage = cobj.getString("response_desc");
            } else {
                postlistreecordsno = cobj.getString("numberOfRecords");

                JSONArray jaArray = cobj.optJSONArray("records");
                postlistarraylength = jaArray.length();


                String[] postlistid = new String[postlistarraylength];
                String[] postlistcontnt = new String[postlistarraylength];
                String[] postlistuserid = new String[postlistarraylength];
                String[] postlistdatei = new String[postlistarraylength];
                String[] postlistactive = new String[postlistarraylength];
                String[] postcomment = new String[postlistarraylength];
                String[] postusername = new String[postlistarraylength];
                String[] postuserpic = new String[postlistarraylength];

                String[] posttitle = new String[postlistarraylength];
                int b = 0;


                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                Date currentLocalTime = cal.getTime();

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
                String localTime = df.format(currentLocalTime);

                postListArrayLengthOriginal = postlistarraylength;

                for (int j = 0; j < postlistarraylength; j++) {
                    JSONObject kobj = jaArray.getJSONObject(j);
                    if (kobj.getString("is_active").equals("yes")) {

                        postlistactive[b] = kobj.getString("is_active");


                        postlistdatei[b] = kobj.getString("comments_lastupdated");

                        try {

                            Date date1 = df.parse(postlistdatei[b]);
                            long diff = (System.currentTimeMillis() - date1.getTime()) / 1000;
                            long timediff = diff / 60;
                            if (timediff > 60) {
                                long timehr = timediff / 60;
                                if (timehr > 24) {
                                    long day = timehr / 24;
                                    postlistdatei[b] = Long.toString(day) + " days ago";
                                } else {
                                    postlistdatei[b] = Long.toString(timehr) + " hours ago";
                                }
                            } else {
                                postlistdatei[b] = Long.toString(timediff) + " minutes ago";
                            }

                        } catch (Exception e) {

                        }

                        postlistuserid[b] = kobj.getString("user_id");
                        postlistcontnt[b] = kobj.getString("post_topic");
                        postlistid[b] = kobj.getString("post_id");
                        postcomment[b] = kobj.getString("total_comments");
                        postusername[b] = kobj.getString("user");
                        if (kobj.has("post_title") && !kobj.getString("post_title").equals("")) {
                            posttitle[b] = kobj.getString("post_title");
                        } else {
                            posttitle[b] = "";
                        }

                        if (kobj.has("profile_picture") && !kobj.getString("profile_picture").equals("")) {
                            postuserpic[b] = kobj.getString("profile_picture");
                        } else {
                            postuserpic[b] = "";
                        }
                        b++;

                    }

                }
                Log.e("posssttss", "=" + b + "  " + postlistid.length);
                postlistarraylength = b;
                if (b > 0) {
                    ParserWeb.postlistid = new String[b];
                    ParserWeb.postlistcontnt = new String[b];
                    ParserWeb.postlistuserid = new String[b];
                    ParserWeb.postlistdatei = new String[b];
                    ParserWeb.postlistactive = new String[b];
                    ParserWeb.postcomment = new String[b];
                    ParserWeb.postusername = new String[b];
                    ParserWeb.postuserpic = new String[b];
                    ParserWeb.posttitle = new String[b];
                    System.arraycopy(postcomment, 0, ParserWeb.postcomment, 0, b);
                    System.arraycopy(postlistcontnt, 0, ParserWeb.postlistcontnt, 0, b);
                    System.arraycopy(postlistuserid, 0, ParserWeb.postlistuserid, 0, b);
                    System.arraycopy(postlistdatei, 0, ParserWeb.postlistdatei, 0, b);
                    System.arraycopy(postlistactive, 0, ParserWeb.postlistactive, 0, b);
                    System.arraycopy(postlistid, 0, ParserWeb.postlistid, 0, b);
                    System.arraycopy(postusername, 0, ParserWeb.postusername, 0, b);
                    System.arraycopy(postuserpic, 0, ParserWeb.postuserpic, 0, b);
                    System.arraycopy(posttitle, 0, ParserWeb.posttitle, 0, b);

                }

                Log.e("possstt", "=" + b + "  " + ParserWeb.postlistid.length);


            }

        } catch (Exception e) {
            e.printStackTrace();
            ParserWeb.postlistid = new String[0];
            ParserWeb.postlistcontnt = new String[0];
            ParserWeb.postlistuserid = new String[0];
            ParserWeb.postlistdatei = new String[0];
            ParserWeb.postlistactive = new String[0];
            ParserWeb.postcomment = new String[0];
            ParserWeb.postusername = new String[0];
            ParserWeb.postuserpic = new String[0];
            ParserWeb.posttitle = new String[0];
            postlistarraylength = 0;
        }
        return postlistresp;
    }

    public static String[] getpostidd() {
        return postlistid;
    }

    public static String[] getpostdate() {
        return postlistdatei;
    }

    public static String[] getpostcontent() {
        return postlistcontnt;
    }

    public static String[] getpostuserid() {
        return postlistuserid;
    }

    public static String[] getpostactiveststus() {
        return postlistactive;
    }

    public static String getpostrecordnumber() {
        return postlistreecordsno;
    }

    public static String getpostlistresp() {
        return postlistresp;
    }

    public static int getpostlistarraylength() {
        return postlistarraylength;
    }

    public static String[] getpostcomment() {
        return postcomment;
    }

    public static String[] getpostusername() {
        return postusername;
    }

    public static String[] getpostuserpic() {
        return postuserpic;
    }

    public static String[] getposttitle() {
        return posttitle;
    }


    //Comment List
    static int cmntlistarraylength;
    static String cmntlistresp;
    static String cmntlistreecordsno;
    static String[] cmntlistid;
    static String cmntlisterror;
    static String[] cmntlistcontnt;
    static String[] cmntlistuserid;
    static String[] cmntlistdatei;
    static String[] cmntlistpostid;
    static String[] commentactive;
    static String[] commentprofilepic;

    public static String commentlist(String responsecommentlist) {

        try {
            JSONObject cobj = new JSONObject(responsecommentlist);
            cmntlistresp = cobj.getString("response");
            if (cmntlistresp.equals("Error")) {
                cmntlisterror = cobj.getString("response_desc");
            } else {
                cmntlistreecordsno = cobj.getString("numberOfRecords");

                JSONArray jaArray = cobj.optJSONArray("records");
                cmntlistarraylength = jaArray.length();

                String[] cmntlistid = new String[cmntlistarraylength];
                String[] cmntlistcontnt = new String[cmntlistarraylength];
                String[] cmntlistuserid = new String[cmntlistarraylength];
                String[] cmntlistdatei = new String[cmntlistarraylength];
                String[] cmntlistpostid = new String[cmntlistarraylength];
                String[] commentactive = new String[cmntlistarraylength];
                String[] commentprofilepic = new String[cmntlistarraylength];


                int b = 0;

                for (int j = 0; j < cmntlistarraylength; j++) {
                    JSONObject kobj = jaArray.getJSONObject(j);
                    if (kobj.getString("is_active").equals("yes")) {
                        commentactive[b] = kobj.getString("is_active");
                        cmntlistid[b] = kobj.getString("comment_id");
                        cmntlistcontnt[b] = kobj.getString("comment");
                        cmntlistuserid[b] = kobj.getString("user_name");
                        cmntlistdatei[b] = kobj.getString("date_created");
                        cmntlistpostid[b] = kobj.getString("post_id");
                        if (kobj.has("profile_picture") && !kobj.getString("profile_picture").equals("")) {
                            commentprofilepic[b] = kobj.getString("profile_picture");
                        } else {
                            commentprofilepic[b] = "";
                        }

                        Log.e("ststs", "=" + kobj.getString("user_name"));
                        b++;
                    }
                }
                cmntlistarraylength = b;


                ParserWeb.cmntlistid = new String[b];
                ParserWeb.cmntlistcontnt = new String[b];
                ParserWeb.cmntlistuserid = new String[b];
                ParserWeb.cmntlistdatei = new String[b];
                ParserWeb.cmntlistpostid = new String[b];
                ParserWeb.commentactive = new String[b];
                ParserWeb.commentprofilepic = new String[b];
                if (b > 0) {

                    System.arraycopy(cmntlistcontnt, 0, ParserWeb.cmntlistcontnt, 0, b);
                    System.arraycopy(cmntlistid, 0, ParserWeb.cmntlistid, 0, b);
                    System.arraycopy(cmntlistuserid, 0, ParserWeb.cmntlistuserid, 0, b);
                    System.arraycopy(cmntlistdatei, 0, ParserWeb.cmntlistdatei, 0, b);
                    System.arraycopy(cmntlistpostid, 0, ParserWeb.cmntlistpostid, 0, b);
                    System.arraycopy(commentactive, 0, ParserWeb.commentactive, 0, b);
                    System.arraycopy(commentprofilepic, 0, ParserWeb.commentprofilepic, 0, b);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            cmntlistarraylength = 0;


            ParserWeb.cmntlistid = new String[0];
            ParserWeb.cmntlistcontnt = new String[0];
            ParserWeb.cmntlistuserid = new String[0];
            ParserWeb.cmntlistdatei = new String[0];
            ParserWeb.cmntlistpostid = new String[0];
            ParserWeb.commentactive = new String[0];
            ParserWeb.commentprofilepic = new String[0];


        }
        return cmntlistresp;
    }

    public static String getcmntlistresp() {
        return cmntlistresp;
    }

    public static int getcmntlistarraylength() {
        return cmntlistarraylength;
    }

    public static String[] getcmntidd() {
        return cmntlistid;
    }

    public static String[] getcmntdate() {
        return cmntlistdatei;
    }

    public static String[] getcmntcontent() {
        return cmntlistcontnt;
    }

    public static String[] getcmntpostuserid() {
        return cmntlistpostid;
    }

    public static String[] getcmntuserid() {
        return cmntlistuserid;
    }

    public static String getcmntlisterror() {
        return cmntlisterror;
    }

    public static String[] getprofilepiccomment() {
        return commentprofilepic;
    }


    //Make comment
    static String cmntresp;
    static String cmnntid;
    static String cmnterrormessage;

    public static String makecmnt(String responsecmnt) {
        try {
            JSONObject cobj = new JSONObject(responsecmnt);

            cmntresp = cobj.getString("response");
            if (cmntresp.equals("Error")) {
                cmnterrormessage = cobj.getString("response_desc");
            } else {
                cmnntid = cobj.getString("commentID");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cmntresp;
    }

    public static String getcoment() {
        return cmntresp;
    }

    public static String getcmntid() {
        return cmnntid;
    }

    public static String getcmnterror() {
        return cmnterrormessage;
    }


    //DoctorActivity Search
    static int doctorarraylength;
    static String doctrresp;
    static String doctorerror;
    static String doctrrecordno;
    static String[] doctor_id;
    static String[] doctr_fname;
    static String[] doctr_secnfname;
    static String[] doctr_profpic;
    static String[] doctr_decs;
    static String[] doctr_addrarea;
    static String[] doctr_addrcity;
    static String[] doctr_addrstate;
    static String[] doctr_addrzip;
    static String[] doctr_geoid;
    static String[] doctr_landmark;
    static String[] doctr_mobile;
    static String[] doctr_land;
    static String[] opnentime1;
    static String[] opnentime2;
    static String[] remark;
    static String[] doctr_lat;
    static String[] doctr_long;
    static String[] doctr_qualifi;
    static String[] doctr_special;

    public static String doctorsearch(String responsedoctorsearch) {
        try {
            JSONObject cobj = new JSONObject(responsedoctorsearch);
            doctrresp = cobj.getString("response");
            if (doctrresp.equals("Error")) {
                doctorerror = cobj.getString("response_desc");
            } else {
                doctrrecordno = cobj.getString("numberOfRecords");

                JSONArray jaArray = cobj.optJSONArray("records");
                doctorarraylength = jaArray.length();

                doctor_id = new String[doctorarraylength];
                doctr_fname = new String[doctorarraylength];
                doctr_secnfname = new String[doctorarraylength];
                doctr_profpic = new String[doctorarraylength];
                doctr_decs = new String[doctorarraylength];
                doctr_addrarea = new String[doctorarraylength];
                doctr_addrcity = new String[doctorarraylength];
                doctr_addrstate = new String[doctorarraylength];
                doctr_addrzip = new String[doctorarraylength];
                doctr_geoid = new String[doctorarraylength];
                doctr_landmark = new String[doctorarraylength];
                doctr_mobile = new String[doctorarraylength];
                doctr_land = new String[doctorarraylength];
                opnentime1 = new String[doctorarraylength];
                opnentime2 = new String[doctorarraylength];
                remark = new String[doctorarraylength];
                doctr_lat = new String[doctorarraylength];
                doctr_long = new String[doctorarraylength];
                doctr_qualifi = new String[doctorarraylength];
                doctr_special = new String[doctorarraylength];


                for (int j = 0; j < doctorarraylength; j++) {
                    JSONObject kobj = jaArray.getJSONObject(j);

                    if (kobj.has("doctor_id") && !kobj.getString("doctor_id").equals("")) {
                        doctor_id[j] = kobj.getString("doctor_id");
                    } else {
                        doctor_id[j] = "";
                    }
                    if (kobj.has("first_name") && !kobj.getString("first_name").equals("")) {
                        doctr_fname[j] = kobj.getString("first_name");
                    } else {
                        doctr_fname[j] = "";
                    }
                    if (kobj.has("last_name") && !kobj.getString("last_name").equals("")) {
                        doctr_secnfname[j] = kobj.getString("last_name");
                    } else {
                        doctr_secnfname[j] = "";
                    }
                    if (kobj.has("profile_picture") && !kobj.getString("profile_picture").equals("")) {
                        doctr_profpic[j] = kobj.getString("profile_picture");
                    } else {
                        doctr_profpic[j] = "";
                    }
                    if (kobj.has("description") && !kobj.getString("description").equals("")) {
                        doctr_decs[j] = kobj.getString("description");
                    } else {
                        doctr_decs[j] = "";
                    }
                    if (kobj.has("area") && !kobj.getString("area").equals("")) {
                        doctr_addrarea[j] = kobj.getString("area");
                    } else {
                        doctr_addrarea[j] = "";
                    }

                    if (kobj.has("city") && !kobj.getString("city").equals("")) {
                        doctr_addrcity[j] = kobj.getString("city");
                    } else {
                        doctr_addrcity[j] = "";
                    }
                    if (kobj.has("state") && !kobj.getString("state").equals("")) {
                        doctr_addrstate[j] = kobj.getString("state");
                    } else {
                        doctr_addrstate[j] = "";
                    }
                    if (kobj.has("zip") && !kobj.getString("zip").equals("")) {
                        doctr_addrzip[j] = kobj.getString("zip");
                    } else {
                        doctr_addrzip[j] = "";
                    }
                    if (kobj.has("geolocation_id") && !kobj.getString("geolocation_id").equals("")) {
                        doctr_geoid[j] = kobj.getString("geolocation_id");
                    } else {
                        doctr_geoid[j] = "";
                    }
                    if (kobj.has("landmark") && !kobj.getString("landmark").equals("")) {
                        doctr_landmark[j] = kobj.getString("landmark");
                    } else {
                        doctr_landmark[j] = "";
                    }
                    if (kobj.has("mobile") && !kobj.getString("mobile").equals("")) {
                        doctr_mobile[j] = kobj.getString("mobile");
                    } else {
                        doctr_mobile[j] = "";
                    }
                    if (kobj.has("landline") && !kobj.getString("landline").equals("")) {
                        doctr_land[j] = kobj.getString("landline");
                    } else {
                        doctr_land[j] = "";
                    }
                    if (kobj.has("opd_1") && !kobj.getString("opd_1").equals("")) {
                        opnentime1[j] = kobj.getString("opd_1");
                    } else {
                        opnentime1[j] = "";
                    }
                    if (kobj.has("opd_2") && !kobj.getString("opd_2").equals("")) {
                        opnentime2[j] = kobj.getString("opd_2");
                    } else {
                        opnentime2[j] = "";
                    }
                    if (kobj.has("remark") && !kobj.getString("remark").equals("")) {
                        remark[j] = kobj.getString("remark");
                    } else {
                        remark[j] = "";
                    }
                    if (kobj.has("latitude") && !kobj.getString("latitude").equals("")) {
                        doctr_lat[j] = kobj.getString("latitude");
                    } else {
                        doctr_lat[j] = "";
                    }
                    if (kobj.has("longitude") && !kobj.getString("longitude").equals("")) {
                        doctr_long[j] = kobj.getString("longitude");
                    } else {
                        doctr_long[j] = "";
                    }
                    if (kobj.has("doctor_qualifications") && !kobj.getString("doctor_qualifications").equals("")) {
                        doctr_qualifi[j] = kobj.getString("doctor_qualifications");
                    } else {
                        doctr_qualifi[j] = "";
                    }
                    if (kobj.has("doctor_specializations") && !kobj.getString("doctor_specializations").equals("")) {
                        doctr_special[j] = kobj.getString("doctor_specializations");
                    } else {
                        doctr_special[j] = "";
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctrresp;
    }

    public static int doctorarraylength() {
        return doctorarraylength;
    }

    public static String getdoctrorecordnumber() {
        return doctrrecordno;
    }

    public static String[] doctor_id() {
        return doctor_id;
    }

    public static String[] doctr_fname() {
        return doctr_fname;
    }

    public static String[] doctr_secnfname() {
        return doctr_secnfname;
    }

    public static String[] doctr_profpic() {

        return doctr_profpic;
    }

    public static String[] doctr_decs() {
        return doctr_decs;
    }


    public static String[] doctr_geoid() {
        return doctr_geoid;
    }

    public static String[] doctr_landmark() {
        return doctr_landmark;
    }

    public static String[] doctr_mobile() {
        return doctr_mobile;
    }

    public static String[] doctr_land() {
        return doctr_land;
    }

    public static String[] opnentime1() {
        return opnentime1;
    }

    public static String[] opnentime2() {
        return opnentime2;
    }

    public static String[] remark() {
        return remark;
    }

    public static String[] doctr_lat() {
        return doctr_lat;
    }

    public static String[] doctr_long() {
        return doctr_long;
    }

    public static String[] doctr_qualifi() {
        return doctr_qualifi;
    }

    //
    public static String[] doctr_special() {
        return doctr_special;
    }

    public static String doctorerror() {
        return doctorerror;
    }

    public static String[] doctr_addrarea() {
        return doctr_addrarea;
    }

    public static String[] doctr_addrcity() {
        return doctr_addrcity;
    }

    public static String[] doctr_addrstate() {
        return doctr_addrstate;
    }

    public static String[] doctr_addrzip() {
        return doctr_addrzip;
    }


    //Recent Search
    static int rdoctorarraylength;
    static String rdoctrresp;
    static String rdoctorerror;
    static String rdoctrrecordno;
    static String[] rdoctor_id;
    static String[] rdoctr_fname;
    static String[] rdoctr_secnfname;
    static String[] rdoctr_profpic;
    static String[] rdoctr_decs;
    static String[] rdoctr_addrarea;
    static String[] rdoctr_addrcity;
    static String[] rdoctr_addrstate;
    static String[] rdoctr_addrzip;
    static String[] rdoctr_geoid;
    static String[] rdoctr_landmark;
    static String[] rdoctr_mobile;
    static String[] rdoctr_land;
    static String[] ropnentime1;
    static String[] ropnentime2;
    static String[] rremark;
    static String[] rdoctr_lat;
    static String[] rdoctr_long;
    static String[] rdoctr_qualifi;
    static String[] rdoctr_special;

    public static String recentsearch(String responsedoctorsearch) {
        try {
            JSONObject cobj = new JSONObject(responsedoctorsearch);
            rdoctrresp = cobj.getString("response");
            if (rdoctrresp.equals("Error")) {
                rdoctorerror = cobj.getString("response_desc");
            } else {
                rdoctrrecordno = cobj.getString("numberOfRecords");

                JSONArray jaArray = cobj.optJSONArray("records");
                rdoctorarraylength = jaArray.length();

                rdoctor_id = new String[rdoctorarraylength];
                rdoctr_fname = new String[rdoctorarraylength];
                rdoctr_secnfname = new String[rdoctorarraylength];
                rdoctr_profpic = new String[rdoctorarraylength];
                rdoctr_decs = new String[rdoctorarraylength];
                rdoctr_addrarea = new String[rdoctorarraylength];
                rdoctr_addrcity = new String[rdoctorarraylength];
                rdoctr_addrstate = new String[rdoctorarraylength];
                rdoctr_addrzip = new String[rdoctorarraylength];
                rdoctr_geoid = new String[rdoctorarraylength];
                rdoctr_landmark = new String[rdoctorarraylength];
                rdoctr_mobile = new String[rdoctorarraylength];
                rdoctr_land = new String[rdoctorarraylength];
                ropnentime1 = new String[rdoctorarraylength];
                ropnentime2 = new String[rdoctorarraylength];
                rremark = new String[rdoctorarraylength];
                rdoctr_lat = new String[rdoctorarraylength];
                rdoctr_long = new String[rdoctorarraylength];
                rdoctr_qualifi = new String[rdoctorarraylength];
                rdoctr_special = new String[rdoctorarraylength];


                for (int j = 0; j < rdoctorarraylength; j++) {
                    JSONObject kobj = jaArray.getJSONObject(j);
                    rdoctor_id[j] = kobj.getString("doctor_id");
                    rdoctr_fname[j] = kobj.getString("first_name");
                    rdoctr_secnfname[j] = kobj.getString("last_name");
                    rdoctr_profpic[j] = kobj.getString("profile_picture");
                    rdoctr_decs[j] = kobj.getString("description");
                    rdoctr_addrarea[j] = kobj.getString("address");
                    rdoctr_addrcity[j] = kobj.getString("city");
                    rdoctr_addrstate[j] = kobj.getString("state");
                    rdoctr_addrzip[j] = kobj.getString("zip");
                    rdoctr_geoid[j] = kobj.getString("geolocation_id");
                    rdoctr_landmark[j] = kobj.getString("landmark");
                    rdoctr_mobile[j] = kobj.getString("mobile");
                    rdoctr_land[j] = kobj.getString("landline");
                    ropnentime1[j] = kobj.getString("opd_1");
                    ropnentime2[j] = kobj.getString("opd_2");
                    rremark[j] = kobj.getString("remark");
                    rdoctr_lat[j] = kobj.getString("latitude");
                    rdoctr_long[j] = kobj.getString("longitude");
                    rdoctr_qualifi[j] = kobj.getString("doctor_qualifications");
                    rdoctr_special[j] = kobj.getString("doctor_specializations");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rdoctrresp;
    }


    public static int recentaralength() {
        return rdoctorarraylength;
    }

    public static String[] rdoctor_id() {
        return rdoctor_id;

    }

    public static String[] rdoctr_fname() {
        return rdoctr_fname;
    }

    public static String[] rdoctr_secnfname() {
        return rdoctr_secnfname;
    }

    public static String[] rdoctr_profpic() {
        return rdoctr_profpic;
    }

    public static String[] rdoctr_decs() {
        return rdoctr_decs;
    }

    public static String[] rdoctr_addrarea() {
        return rdoctr_addrarea;
    }

    public static String[] rdoctr_addrcity() {
        return rdoctr_addrcity;
    }

    public static String[] rdoctr_addrstate() {
        return rdoctr_addrstate;
    }

    public static String[] rdoctr_addrzip() {
        return rdoctr_addrzip;
    }

    public static String[] rdoctr_geoid() {
        return rdoctr_geoid;
    }

    public static String[] rdoctr_landmark() {
        return rdoctr_landmark;
    }

    public static String[] rdoctr_mobile() {
        return rdoctr_mobile;
    }

    public static String[] rdoctr_land() {
        return rdoctr_land;
    }

    public static String[] ropnentime1() {
        return ropnentime1;
    }

    public static String[] ropnentime2() {
        return ropnentime2;
    }

    public static String[] rremark() {
        return rremark;
    }

    public static String[] rdoctr_lat() {
        return rdoctr_lat;
    }

    public static String[] rdoctr_long() {
        return rdoctr_long;
    }

    public static String rdoctorerror() {
        return rdoctorerror;
    }

    public static String[] doctr_rqualifi() {
        return rdoctr_qualifi;
    }

    public static String[] doctr_rspecial() {
        return rdoctr_special;
    }

    ///Get user profile
    static String[] firstnameed;
    static String[] lastnameed;
    static String[] getgender;
    static String getrecordno;
    static String getprofresponse;
    static int getprofarray;
    static String[] getage;
    static String[] getprofpic;
    static String[] getmemberoffamily;
    static String[] getfamilyrole;
    static String[] getmartial;


    public static String getprof(String responseLogin) {
        try {
            JSONObject cobj = new JSONObject(responseLogin);
            getprofresponse = cobj.getString("response");
            if (getprofresponse.equals("Error")) {
                loginstatuserror = cobj.getString("response_desc");
            }
            getrecordno = cobj.getString("numberOfRecords");

            JSONArray jaArray = cobj.optJSONArray("records");
            getprofarray = jaArray.length();
            firstnameed = new String[getprofarray];
            lastnameed = new String[getprofarray];
            getprofpic = new String[getprofarray];
            getmemberoffamily = new String[getprofarray];

            getfamilyrole = new String[getprofarray];
            getmartial = new String[getprofarray];
            getage = new String[getprofarray];
            getgender = new String[getprofarray];

            for (int j = 0; j < getprofarray; j++) {
                JSONObject kobj = jaArray.getJSONObject(j);
                if (kobj.has("first_name")) {
                    firstnameed[j] = kobj.getString("first_name");
                } else {
                    firstnameed[j] = "";
                }
                if (kobj.has("last_name")) {
                    lastnameed[j] = kobj.getString("last_name");
                } else {
                    lastnameed[j] = "";
                }
                if (kobj.has("marital_status_id")) {
                    getmartial[j] = kobj.getString("marital_status_id");
                } else {
                    getmartial[j] = "";
                }
                if (kobj.has("family_role_id")) {
                    getfamilyrole[j] = kobj.getString("family_role_id");
                } else {
                    getfamilyrole[j] = "";
                }
                if (kobj.has("age")) {
                    getage[j] = kobj.getString("age");

                } else {
                    getage[j] = "";
                }
                if (kobj.has("members_in_family")) {
                    getmemberoffamily[j] = kobj.getString("members_in_family");
                } else {
                    getmemberoffamily[j] = "";
                }

                if ((kobj.has("profile_picture")) && !kobj.getString("profile_picture").equals("")) {
                    getprofpic[j] = kobj.getString("profile_picture");
                } else {
                    getprofpic[j] = "";
                }
                if ((kobj.has("gender")) && !kobj.getString("gender").equals("")) {
                    getgender[j] = kobj.getString("gender");
                } else {
                    getgender[j] = "";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getprofresponse;
    }

    public static String getgetprof() {
        return getprofresponse;
    }

    public static String getprofrecord() {
        return getrecordno;
    }

    public static int getprofarray() {
        return getprofarray;
    }

    public static String[] getfamilyid() {
        return getfamilyrole;
    }

    public static String[] getage() {
        return getage;
    }

    public static String[] getmartialststus() {
        return getmartial;
    }

    public static String[] getprofpic() {
        return getprofpic;
    }

    public static String[] getfamilymember() {
        return getmemberoffamily;
    }

    public static String[] getgender() {
        return getgender;
    }

    public static String[] getfirstnameed() {
        return firstnameed;
    }

    public static String[] getlastnameed() {
        return lastnameed;
    }

    //Article like
    static String likeststus;

    static String likeerror;

    public static String likearticle(String data) {
        try {
            JSONObject dataObject = new JSONObject(data);

            likeststus = dataObject.optString("response");

            if (likeststus.equals("Error")) {
                likeerror = dataObject.optString("response_desc");
            }
        } catch (Exception e) {

        }
        return likeststus;
    }


    //Article dislike

    static String dlikeststus;

    static String dlikeerror;

    public static String dislikearticle(String data) {
        try {
            JSONObject dataObject = new JSONObject(data);

            dlikeststus = dataObject.optString("response");

            if (dlikeststus.equals("Error")) {
                dlikeerror = dataObject.optString("response_desc");
            }
        } catch (Exception e) {

        }
        return dlikeststus;
    }

    //Video like
    static String vlikeststus;

    static String vlikeerror;

    public static String likevideo(String data) {
        try {
            JSONObject dataObject = new JSONObject(data);

            vlikeststus = dataObject.optString("response");

            if (vlikeststus.equals("Error")) {
                vlikeerror = dataObject.optString("response_desc");
            }
        } catch (Exception e) {

        }
        return vlikeststus;
    }

    //Video dislike
    static String vdlikeststus;

    static String vdlikeerror;

    public static String dislikevideo(String data) {
        try {
            JSONObject dataObject = new JSONObject(data);

            vdlikeststus = dataObject.optString("response");

            if (vdlikeststus.equals("Error")) {
                vdlikeerror = dataObject.optString("response_desc");
            }
        } catch (Exception e) {

        }
        return vdlikeststus;
    }

}


