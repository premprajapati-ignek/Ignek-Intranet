<style>
.media-images-container .media-container {
	height: 290px;
	border-radius: 15px !important;
	overflow: hidden;
}
.media-images-container .btn-view-more {
	background-color: white;
	height: 36px;
	width: 130px;
	border-radius: 4px;
	border: 1px solid #00979e;
	color: #00979e;
	cursor: pointer;
	transition: background-color 0.3s ease;
}
.media-images-container .btn-view-more:hover {
	background-color: #00979e;
	color: white;
}
.media-images-container .hidden-img {
	display: none;
}
</style>

<#if entries?has_content>
    <div class="container-fluid media-images-container">
        <h2 class="poppins-semibold mb-4">${languageUtil.get(locale, 'media-feild-title')}</h2>
        <div class="row" id="image-gallery-row">
            <#assign imageMimeTypes = propsUtil.getArray("dl.file.entry.preview.image.mime.types") />
            <#assign count = 0 />

            <#list entries as entry>
                <#if imageMimeTypes?seq_contains(entry.getMimeType())>
                    <div class="col-xs-12 col-sm-6 col-md-4 col-lg-4 mb-3 <#if count gte 3>hidden-img</#if>">
                        <div class="card media-container">
                            <img alt="${htmlUtil.escapeAttribute(entry.getDescription())}"
                                 src="${dlUtil.getPreviewURL(entry, entry.getFileVersion(), themeDisplay, "")}"
                                 class="card-img-top"
                                 style="height:100%; object-fit: cover;" />
                        </div>
                    </div>
                    <#assign count = count + 1 />
                </#if>
            </#list>
        </div>
        <div class="d-flex flex-row-reverse">
            <button class="btn-view-more poppins-regular" id="toggle-images-btn">${languageUtil.get(locale, 'view-more-btn')}</button>
        </div>
    </div>
</#if>


<script>
    document.addEventListener("DOMContentLoaded", () => {
        const toggleBtn = document.getElementById("toggle-images-btn");
        const hiddenImages = document.querySelectorAll(".hidden-img");

        if (hiddenImages.length === 0) {
            if (toggleBtn) {
                toggleBtn.style.display = 'none';
            }
            return;
        }

        toggleBtn.addEventListener("click", () => {
            if (toggleBtn.textContent.trim() === "View More") {
                hiddenImages.forEach((img) => {
                    img.classList.remove("hidden-img");
                });
                toggleBtn.textContent = "${languageUtil.get(locale, 'view-less-btn')}";
            } else if(toggleBtn.textContent.trim() === "View Less"){
                hiddenImages.forEach((img) => {
                    img.classList.add("hidden-img");
                });
                toggleBtn.textContent = "${languageUtil.get(locale, 'view-more-btn')}";
            }
        });
    });
</script>