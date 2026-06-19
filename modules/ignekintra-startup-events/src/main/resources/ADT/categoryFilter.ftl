<#assign hasCategories = false />

<#if entries?has_content>
	<@clay.row>
		<#list entries as entry>
			<#assign categories = entry.getCategories() />

			<#if categories?has_content>
				<#assign hasCategories = true />
				<@clay.col md="6">
					<@displayCategories categories = categories />
				</@clay.col>
				</#if>
			</#list>

		<#if !hasCategories>
			${renderRequest.setAttribute("PORTLET_CONFIGURATOR_VISIBILITY", true)}
			<div class="alert alert-info w-100">
				<@liferay_ui.message key="there-are-no-categories" />
			</div>
			</#if>
		</@clay.row>
	</#if>

<#macro displayCategories
				categories
				>
	<#if categories?has_content>
		<ul class="category-filter-container">
			<!-- <li class="category-items">
				<a class="category-link poppins-regular" href="#">All</a>
			</li> -->
			<#list categories as category>
				<li class="category-items">
					<#assign categoryURL = renderResponse.createRenderURL() />
					<#assign categoryIdParam = renderRequest.getParameter("categoryId")!"0" />
					<#assign isActive = (category.getCategoryId()?string == categoryIdParam) />

					${categoryURL.setParameter("resetCur", "true")}
					${categoryURL.setParameter("categoryId", category.getCategoryId()?string)}

					<a class="category-link poppins-regular ${(isActive)?then('active', '')}" href="${categoryURL}">${category.getName()}</a>

					<#if serviceLocator??>
						<#assign
										 assetCategoryService = serviceLocator.findService("com.liferay.asset.kernel.service.AssetCategoryService")

										 childCategories = assetCategoryService.getChildCategories(category.getCategoryId())
										 />

						<@displayCategories categories = childCategories />
						</#if>
				</li>
				</#list>
		</ul>
		</#if>
	</#macro>
<style>
	.category-filter-container {
		margin:1rem 1rem 1rem 0;
		padding-left: 0;
		display: flex;
		list-style-type: none;
		align-items: center;
	}
	.category-filter-container .category-items {
		margin: 0 0.5rem 0 0.5rem;
	}
	.category-filter-container .category-link {
		border: 1px solid #26262680;
		padding: 10px 29px 10px 29px;
		border-radius: 36px;
		cursor: pointer;
		text-decoration: none;
		color: #262626;
		transition: background-color 0.3s ease;
	}
	.category-filter-container .category-link:hover {
		background-color: #00979e;
		color: #ffffff;
	}
	.category-link.active {
		background-color: #00979e;
		color: #ffffff;
	}
</style>