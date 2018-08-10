package ru.croc.ctp.mts;

import static com.google.common.collect.Maps.newHashMap;

import org.springframework.stereotype.Component;

import ru.croc.ctp.jxfw.wc.web.config.ConfigModuleModifier;
import ru.croc.ctp.jxfw.wc.web.config.XConfig;
import ru.croc.ctp.jxfw.wc.web.config.XSecurityConfig;

import java.util.Map;

/**
 * Class describes configuration of client part.
 * 
 * @author team1
 *
 */
@Component
public class MtsConfig implements ConfigModuleModifier {

	/**
	 * Method creates client configuration.
	 */
	@Override
	public XConfig createClientConfig(XConfig config) {

		Map<String, Object> modules = config.getModules();
		if (modules == null) {
			modules = newHashMap();
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> files = (Map<String, Object>) modules.get("files");
		if (files == null) {
			files = newHashMap();
		}
		files.put("apiRoute", "api/_file");
		modules.put("files", files);
		config.setModules(modules);

		final XSecurityConfig security = new XSecurityConfig();

		final Map<Object, Object> formsAuth = newHashMap();
		formsAuth.put("loginUrl", "/login");
		formsAuth.put("isDefault", true);

		security.put("formsAuth", formsAuth);

		security.put("logoutUrl", "/logout");
		config.setSecurity(security);

		return config;
	}
}
