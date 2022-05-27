package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
//import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public List<GetUserRes> getUsers(){
//        String getUsersQuery = "select * from UserInfo";
//        return this.jdbcTemplate.query(getUsersQuery,
//                (rs,rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password"))
//                );
//    }
//
//    public List<GetUserRes> getUsersByEmail(String email){
//        String getUsersByEmailQuery = "select * from UserInfo where email =?";
//        String getUsersByEmailParams = email;
//        return this.jdbcTemplate.query(getUsersByEmailQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password")),
//                getUsersByEmailParams);
//    }
//
//    public GetUserRes getUser(int userIdx){
//        String getUserQuery = "select * from UserInfo where userIdx = ?";
//        int getUserParams = userIdx;
//        return this.jdbcTemplate.queryForObject(getUserQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password")),
//                getUserParams);
//    }

    public GetPhoneUserRes getUserEmailByPhone(String phone){
        String getUserEmailByPhoneQuery = "select email from user where phone = ?";
        return this.jdbcTemplate.queryForObject(getUserEmailByPhoneQuery,
                (rs, rowNum) -> new GetPhoneUserRes(
                        true,
                        rs.getString("email")),
                phone);
    }

    

    public int createUser(PostSignUpReq postSignUpReq){
        String createUserQuery = "insert into user (email, name, phone, password) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postSignUpReq.getEmail(), postSignUpReq.getName(), postSignUpReq.getPhone(), postSignUpReq.getPassword()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int setTokens(int userId, String jwt, String refreshJwt){
        String setUserTokensQuery = "update user set access_token = ?, refresh_token = ? where user_id = ?";
        Object[] setUserTokensParams = new Object[]{jwt, refreshJwt, userId};
        return this.jdbcTemplate.update(setUserTokensQuery, setUserTokensParams);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public int checkPhone(String phone){
        String checkPhoneQuery = "select exists(select phone from user where phone = ?)";
        String checkPhoneParams = phone;
        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkPhoneParams);
    }

    public String getUserAccessToken(int userId){
        String getUserAccessTokenQuery = "select access_token from user where user_id = ?";
        int getUserAccessTokenParam = userId;
        return this.jdbcTemplate.queryForObject(getUserAccessTokenQuery,
                String.class,
                getUserAccessTokenParam);
    }
    public String getUserRefreshToken(int userId){
        String getUserRefreshTokenQuery = "select refresh_token from user where user_id = ?";
        int getUserRefreshTokenParam = userId;
        return this.jdbcTemplate.queryForObject(getUserRefreshTokenQuery,
                String.class,
                getUserRefreshTokenParam);
    }


    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getUser(PostSignInReq postSignInReq){
        String getUserQuery = "select user_id, name, email, password, status, phone from user where email = ?";
        String getUserParam = postSignInReq.getEmail();

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("status"),
                        rs.getString("phone")
                ),
                getUserParam
                );
    }

    public int checkId(int userId){
        String checkIdQuery = "select exists(select user_id from user where user_id = ?)";
        int checkIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                checkIdParams);
    }

    public GetUserAddressRes getUserAddress(int userId, Boolean isSelected){
        String getUserAddressQuery = "select user_id, address_name from user_address where user_id = ? AND is_selected = ?";

        Object[] getUserAddressParams = new Object[]{userId, isSelected};
        return this.jdbcTemplate.queryForObject(getUserAddressQuery,
                (rs, rowNum) -> new GetUserAddressRes(
                        rs.getInt("user_id"),
                        rs.getString("address_name")
                ),
                getUserAddressParams);
    }

    //core 추가
    public List<GetOrderRes> getOrdersByDeliveryStatus(int user_id, int delivery_status) {
        String getUsersByNicknameQuery = "select `order`.order_id, `order`.restaurant_id, user_id, delivery_status, order_total_price, url from `order`\n" +
                "inner join receipt on `order`.order_id = receipt.order_id\n" +
                "inner join res_image on res_image.restaurant_id = `order`.restaurant_id\n" +
                "where (user_id = ? and delivery_status = ?);"; // 해당 이메일을 만족하는 유저를 조회하는 쿼리문
        Object[] getOrderParams = new Object[]{user_id, delivery_status};
        return this.jdbcTemplate.query(getUsersByNicknameQuery,
                (rs, rowNum) -> new GetOrderRes(
                        rs.getInt("order_id"),
                        rs.getInt("restaurant_id"),
                        rs.getInt("user_id"),
                        rs.getInt("delivery_status"),
                        rs.getInt("order_total_price"),
                        rs.getString("url")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getOrderParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    //core 추가
    public List<GetReceiptRes> getReceipts(int userId, int orderId) {
        String getReceiptsQuery = "select receipt_id, receipt.order_id, menu_name, pay_info, discount_fee, pay_by, delivery_fee, order_price, delivery_address, order.order_total_price, user.user_id from receipt\n" +
                "inner join `order` on `order`.order_id = receipt.order_id\n" +
                "inner join user on receipt.user_id = user.user_id\n" +
                "where (user.user_id = ? and `order`.order_id = ?);";
        Object[] getReceiptParams = new Object[]{userId, orderId};
        return this.jdbcTemplate.query(getReceiptsQuery,
                (rs, rowNum) -> new GetReceiptRes(
                        rs.getInt("receipt_id"),
                        rs.getInt("order_id"),
                        rs.getInt("order_total_price"),
                        rs.getString("menu_name"),
                        rs.getString("pay_info"),
                        rs.getInt("discount_fee"),
                        rs.getString("pay_by"),
                        rs.getInt("delivery_fee"),
                        rs.getInt("order_price"),
                        rs.getString("delivery_address"),
                        rs.getInt("user_id")), // RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
                getReceiptParams); // 해당 닉네임을 갖는 모든 User 정보를 얻기 위해 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }
}
