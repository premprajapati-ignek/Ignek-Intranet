<style>
	.technologies-search-results-container {
		padding: 0 20px;
	}
	.technologies-cards {
		border: 1px solid #00000033 !important;
		box-shadow: 0 4px 10px 0 #00000040;
		min-height: 280px;
		border-radius: 15px !important;
	}
	.technologies-cards .card-description {
		padding: 0rem 1rem;
		font-size: 16px;
	}
	.technologies-cards .card-title-text {
		margin: 0 !important;
		padding: 0.5rem 1rem;
		color: #262626;
		font-size: 16px;
	}
	.technologies-cards .card-description-text {
		overflow: hidden;
		display: -webkit-box;
		-webkit-line-clamp: 7;
		-webkit-box-orient: vertical;
		font-size: 16px;
		color: #262626d9;
	}
	.technologies-cards .card-image {
		width: 30px;
		height: 30px;
		object-fit: cover;
	}
	.technologies-cards .card-btn {
		margin-top: 0.5rem;
		display: inline-flex;
		align-items: center;
		justify-content: center;
		background-color: #00979e;
		font-size: 14px;
		height: 36px;
		width: 130px;
		border-radius: 4px;
		color: #ffffff;
		text-decoration: none;
		transition: background-color 0.3s ease;
	}
	.technologies-cards .card-btn:hover {
		background-color: #004e52;
		color: #ffffff;
	}
</style>

<#if entries?has_content>
	<div class="technologies-search-results-container">
		<div class="row">
			<#list entries as entry>
				<div class="col-12 col-sm-6 col-lg-4 mb-4">
					<div class="card-body border rounded technologies-cards h-100">
						<h4 class="card-title poppins-semibold">
							<div class="d-flex align-items-center">
								<p class="card-title-text m-0 ml-2">
									${entry.getHighlightedTitle()}
								</p>
							</div>
						</h4>
						<div class="card-description">
							<div class="poppins-regular card-description-text">
								${entry.getContent()}
							</div>
							<a href="${entry.getViewURL()}" class="poppins-medium card-btn">${languageUtil.get(locale, 'learn-more-text')}</a>
						</div>
					</div>
				</div>
				</#list>
		</div>
	</div>
	<#else>
		<div class="ml-4">
			<h1 class="pt-4 poppins-bold">
				${languageUtil.get(locale, 'no-search-result-found')}
			</h1>
		</div>
		</#if>