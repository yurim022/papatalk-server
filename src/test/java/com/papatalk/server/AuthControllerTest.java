package com.papatalk.server;

import com.papatalk.BaseControllerTest;
import com.papatalk.server.auth.dto.LoginDto;
import com.papatalk.server.auth.entitiy.Member;
import com.papatalk.server.auth.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public class AuthControllerTest extends BaseControllerTest {


}
