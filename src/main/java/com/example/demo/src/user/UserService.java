package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    @Transactional
    public PostSignUpInRes createUser(PostSignUpReq postSignUpReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postSignUpReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        //중복
        if(userProvider.checkPhone(postSignUpReq.getPhone()) ==1){
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postSignUpReq.getPassword());
            postSignUpReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userId = userDao.createUser(postSignUpReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userId);
            String refreshJwt = jwtService.createRefreshJwt();
            userDao.setRefreshToken(userId, refreshJwt);
            return new PostSignUpInRes(userId, jwt, refreshJwt);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public PostSignUpInRes signIn(PostSignInReq postSignInReq) throws BaseException{
        User user = userDao.getUser(postSignInReq);

        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postSignInReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            String refreshJwt = jwtService.createRefreshJwt();

            return new PostSignUpInRes(userId, jwt, refreshJwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public PostSignInJwtRes signInByJwt() throws BaseException{
        try{
            int userId = jwtService.getUserId();
            if(userDao.checkId(userId) != 1){
                throw new BaseException(FAILED_TO_LOGIN_BY_JWT);
            }
            String jwt = jwtService.createJwt(userId);
            return new PostSignInJwtRes(userId, jwt);
        }
        catch (Exception exception){
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

}