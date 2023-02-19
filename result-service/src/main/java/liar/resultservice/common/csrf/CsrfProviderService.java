package liar.resultservice.common.csrf;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

@Service
public class CsrfProviderService {


    public Csrf createCsrf(HttpServletRequest request) {

        CsrfToken token = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
        return Csrf.builder().token(token.getToken()).headerName(token.getHeaderName()).build();
    }

}
