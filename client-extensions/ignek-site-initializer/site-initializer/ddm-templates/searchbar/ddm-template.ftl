<style>
.site-search-container{
	height:66px;
	width:85%;
}
.site-search-container .site-search-input {
	font-size: 24px;
  color: #333333;
	flex:1;
	height:66px;
	border:none;
	border-bottom:1px solid #999999;
}
.site-search-container .site-search-input:focus {
    outline: none;
}
.site-search-container .site-search-icon-container {
	margin-left:1rem;
}
.site-search-container .site-search-icon {
	height:56px;
	width:56px;
	padding:16px;
	border-radius: 50%;
	border: 1px solid #000000;
}
</style>

<@liferay_aui.fieldset cssClass="search-bar" style="padding-left: 0px;">
    <@liferay_aui.input cssClass="search-bar-empty-search-input" name="emptySearchEnabled" type="hidden" value=searchBarPortletDisplayContext.isEmptySearchEnabled() />

    <div class="search-bar-container ${searchBarPortletDisplayContext.isLetTheUserChooseTheSearchScope()?then("search-bar-scope","search-bar-simple")}">
        <#if searchBarPortletDisplayContext.isLetTheUserChooseTheSearchScope()>
            <div class="search-bar-submit-wrapper">
                <@clay["button"] aria\-label="${languageUtil.get(locale, 'search')}" cssClass="search-bar-submit-button" disabled=true displayType="secondary" icon="search" type="submit" />
            </div>

            <@liferay_aui.select cssClass="search-bar-scope-select" disabled=true label="" name=htmlUtil.escape(searchBarPortletDisplayContext.getScopeParameterName()) title="scope" useNamespace=false wrapperCssClass="search-bar-select-container" >
                <@liferay_aui.option label="this-site" selected=searchBarPortletDisplayContext.isSelectedCurrentSiteSearchScope() value=searchBarPortletDisplayContext.getCurrentSiteSearchScopeParameterString() />
                <#if searchBarPortletDisplayContext.isAvailableEverythingSearchScope()>
                    <@liferay_aui.option label="everything" selected=searchBarPortletDisplayContext.isSelectedEverythingSearchScope() value=searchBarPortletDisplayContext.getEverythingSearchScopeParameterString() />
                </#if>
            </@>

            <#assign data = { "test-id": "searchInput" } />
            <@liferay_aui.input autoFocus=true autocomplete="off" cssClass="search-bar-keywords-input" data=data disabled=true label="" name=htmlUtil.escape(searchBarPortletDisplayContext.getKeywordsParameterName()) placeholder=searchBarPortletDisplayContext.getInputPlaceholder() title=languageUtil.get(locale, "search") type="text" useNamespace=false value=htmlUtil.escape(searchBarPortletDisplayContext.getKeywords()) wrapperCssClass="search-bar-input-wrapper" />
        <#else>
            <div class="search-bar-keywords-container d-flex align-items-center site-search-container">
                <input autocomplete="off" class="poppins-regular search-bar-keywords-input site-search-input" data-qa-id="searchInput" disabled=true id="${namespace + stringUtil.randomId()}" name="${htmlUtil.escape(searchBarPortletDisplayContext.getKeywordsParameterName())}" placeholder="${languageUtil.get(locale, 'search-placeholder')}" title="${languageUtil.get(locale, "search")}" type="text" value="${htmlUtil.escape(searchBarPortletDisplayContext.getKeywords())}" />
                <div class="search-bar-submit-wrapper site-search-icon-container">
                    <@clay["button"] aria\-label="${languageUtil.get(locale, 'search')}" cssClass="search-bar-submit-button site-search-icon" disabled=true displayType="unstyled" icon="search" type="submit" />
                </div>
                <@liferay_aui.input name=htmlUtil.escape(searchBarPortletDisplayContext.getScopeParameterName()) type="hidden" value=searchBarPortletDisplayContext.getScopeParameterValue() />
            </div>
        </#if>
    </div>
</@>