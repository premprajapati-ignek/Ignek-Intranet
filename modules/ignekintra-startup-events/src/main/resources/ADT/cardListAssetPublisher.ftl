<div class="asset-card-list">
	<#if entries?has_content>
	<div>
		<div class="row">
			<#list entries as curEntry>
				<div class="col-12 col-sm-6 col-lg-4 mb-4">
					<div>
						<#assign
										 assetRenderer = curEntry.getAssetRenderer()
										 journalArticle = assetRenderer.getAssetObject()
										 />
						<@liferay_journal["journal-article"]
															 articleId=journalArticle.getArticleId()
															 ddmTemplateKey=journalArticle.getDDMTemplateKey()
															 groupId=journalArticle.getGroupId()
															 />
					</div>
				</div>
				</#list>
		</div>
	</div>
	</#if>
</div>

<style>
	.entry-title{
		display: none;
	}
</style>