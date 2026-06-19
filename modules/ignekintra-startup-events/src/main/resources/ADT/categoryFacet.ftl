<style>
.category-facet-container{
	padding: 0 20px;
	display: flex;
	gap: 23px;
}
.category-facet-container .category-facet-label{
	border: 1px solid #26262680;
	padding: 10px 29px 10px 29px;
	border-radius: 36px;
	color: #262626;
	transition: background-color 0.3s ease;
}
.category-facet-container .category-facet-label:hover {
	background-color: #00979e;
	color: #ffffff !important;
	text-decoration: none !important;
}
.category-facet-container .category-facet-label-text{
	font-size:14px;
	line-height: 20px;
}
.category-facet-label.active {
		background-color: #00979e;
		color: #ffffff;
}
</style>

<@liferay_ui["panel-container"]
	extended=true
	id="${namespace + 'facetAssetCategoriesPanelContainer'}"
	markupView="lexicon"
	persistState=true
>
	<#if entries?has_content>	
		<div class="category-facet-container">
				<@clay.button
											cssClass="category-facet-label facet-clear-btn poppins-regular ${(assetCategoriesSearchFacetDisplayContext.isNothingSelected())?then('active', '')}"
											displayType="link"
											id="${namespace + 'facetAssetCategoriesClear'}"
											onClick="Liferay.Search.FacetUtil.clearSelections(event);"
											>
					<span class="category-facet-label-text poppins-regular">${languageUtil.get(locale, "all-btn")}</span>
				</@clay.button>
				<#list entries as entry>
				<@clay.button
											cssClass="category-facet-label facet-term ${(entry.isSelected())?then('active', '')} term-name"
											data\-term\-id="${entry.getFilterValue()}"
											disabled="true"
											displayType="unstyled"
											onClick="Liferay.Search.FacetUtil.changeSelection(event);"
											>
					<span class="category-facet-label-text poppins-regular">
						${htmlUtil.escape(entry.getBucketText())}
					</span>
					</@clay.button>
				</#list>
		</div>
		</#if>
</@>