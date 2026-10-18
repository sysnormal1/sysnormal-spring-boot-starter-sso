package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.auth;

import com.sysnormal.commons.core.DefaultDataSwap;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * O {@code redirect_uri} do login social.
 *
 * O valor é do front: só quem pede o login sabe para onde quer voltar, e o SSO
 * não o configura nem o valida — o provedor já recusa o que não estiver
 * cadastrado no console dele. Mas o parâmetro é obrigatório para o provedor, e
 * quem monta a URL do provedor é o SSO. Por isso ele é exigido aqui, nos dois
 * passos: sem ele, o {@code get_login_url} devolveria uma URL que o provedor
 * recusa só depois do redirecionamento, com uma mensagem que não aponta para nós.
 *
 * Os dois passos usam o mesmo valor, vindo do mesmo lugar — é o que o provedor
 * confere na troca do código.
 */
public final class SocialLoginRedirect {

    public static final String MISSING_MESSAGE = "missing redirectUri";

    private SocialLoginRedirect() {
    }

    /**
     * Recusa a requisição sem {@code redirectUri}.
     *
     * @return o resultado de erro (417) a devolver, ou {@code null} quando o valor veio
     */
    public static DefaultDataSwap rejectIfMissing(String redirectUri) {
        if (StringUtils.hasText(redirectUri)) {
            return null;
        }
        DefaultDataSwap result = new DefaultDataSwap();
        result.httpStatusCode = HttpStatus.EXPECTATION_FAILED.value();
        result.message = MISSING_MESSAGE;
        return result;
    }

    /** O par {@code &redirect_uri=…} já codificado, para emendar na URL do provedor. */
    public static String queryParameter(String redirectUri) {
        return "&redirect_uri=" + URLEncoder.encode(redirectUri.trim(), StandardCharsets.UTF_8);
    }
}
