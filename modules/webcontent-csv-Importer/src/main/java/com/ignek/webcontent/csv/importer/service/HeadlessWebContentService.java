package com.ignek.webcontent.csv.importer.service;

import com.ignek.webcontent.csv.importer.constants.WebcontentCsvImporterPortletKeys;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.osgi.service.component.annotations.Component;

@Component(service =  HeadlessWebContentService.class)

public class HeadlessWebContentService {
    private static final Log log = LogFactoryUtil.getLog(HeadlessWebContentService.class);

    public void createWebContent(JSONArray payload) throws Exception{
        String accessToken = getAccessToken();
        String endpoint = WebcontentCsvImporterPortletKeys.BASE_URL + "/o/headless-delivery/v1.0/structured-content-folders/" +
                WebcontentCsvImporterPortletKeys.FOLDER_ID + "/structured-contents/batch";
        log.info("calling endpoint" + endpoint);
        log.info("Payload Size: " + payload.length());

        HttpResponse<String> response = Unirest.post(endpoint)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body(payload.toString())
                .asString();

        log.info("Response Status: " + response.getStatus());
        log.info("Response Body: " + response.getBody());

        if (response.getStatus() >=300) {
            throw new RuntimeException(response.getBody());
        }
    }

    public String getAccessToken() throws Exception {

        HttpResponse<String> response =
                Unirest.post(WebcontentCsvImporterPortletKeys.BASE_URL + "/o/oauth2/token")
                        .basicAuth(WebcontentCsvImporterPortletKeys.CLIENT_ID, WebcontentCsvImporterPortletKeys.CLIENT_SECRET)
                        .field("grant_type","client_credentials")
                        .asString();

        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(response.getBody());

        return jsonObject.getString(WebcontentCsvImporterPortletKeys.ACCESS_TOKEN);
    }
}