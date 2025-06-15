
import { fetchImages } from './api.js';
import { renderGallery, showLoadingState, showErrorState } from './gallery.js';
import { renderPagination } from './pagination.js';

let currentPage = 0;
const itemsPerPage = 15;

const uploadBtn = document.getElementById('uploadBtn');
const uploadModal = document.getElementById('uploadModal');
const closeModal = document.getElementById('closeModal');
const cancelUpload = document.getElementById('cancelUpload');

document.addEventListener('DOMContentLoaded', () => {
    lucide.createIcons();
    refreshGallery();
});

window.refreshGallery = async () => {
    try {
        showLoadingState();
        const data = await fetchImages(currentPage, itemsPerPage);
        renderGallery(data.images);
        renderPagination(currentPage, data.totalPages);
    } catch (error) {
        showErrorState();
    }
};

window.changePage = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
        currentPage = newPage;
        refreshGallery();
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
};


uploadBtn.addEventListener('click', () => uploadModal.classList.remove('hidden'));
closeModal.addEventListener('click', resetUploadModal);
cancelUpload.addEventListener('click', resetUploadModal);
