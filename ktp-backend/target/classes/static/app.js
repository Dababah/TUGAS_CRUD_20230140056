/**
 * SiMaKTP — Sistem Manajemen KTP
 * Client-side JavaScript using jQuery Ajax
 * All CRUD operations with the Spring Boot REST API
 */

$(function () {
    "use strict";

    /* ============================================================
       CONFIGURATION
    ============================================================ */
    const API_BASE = "/ktp";
    const TOAST_DURATION = 3500;

    /* ============================================================
       STATE
    ============================================================ */
    let allKtpData = [];      // holds the full data for client-side search
    let pendingDeleteId = null;

    /* ============================================================
       INIT
    ============================================================ */
    setCurrentDate();
    loadAllKtp();

    /* ============================================================
       UTILITIES
    ============================================================ */

    /** Display current date in the header badge */
    function setCurrentDate() {
        const opts = { weekday: "long", year: "numeric", month: "long", day: "numeric" };
        $("#current-date").text(new Date().toLocaleDateString("id-ID", opts));
    }

    /** Show a toast notification */
    function showToast(message, type = "info", duration = TOAST_DURATION) {
        const $toast = $("#toast");
        const icons = { success: "✅", error: "❌", warning: "⚠️", info: "ℹ️" };
        $toast
            .removeClass("success error warning info show")
            .addClass(type)
            .html(`<span>${icons[type] || "ℹ️"}</span> ${message}`)
            .addClass("show");

        clearTimeout($toast.data("timer"));
        $toast.data("timer", setTimeout(() => $toast.removeClass("show"), duration));
    }

    /** Format a date string (YYYY-MM-DD) to Indonesian locale */
    function formatDate(dateStr) {
        if (!dateStr) return "-";
        const d = new Date(dateStr + "T00:00:00");
        return d.toLocaleDateString("id-ID", { day: "2-digit", month: "long", year: "numeric" });
    }

    /** Clear all inline form validation errors */
    function clearFormErrors() {
        $(".form-error").text("");
        $(".form-input").removeClass("error");
    }

    /** Show an inline validation error */
    function showFieldError(fieldId, message) {
        $(`#${fieldId}`).addClass("error");
        $(`#${fieldId}-error`).text(message);
    }

    /** Client-side form validation before sending to API */
    function validateForm() {
        clearFormErrors();
        let valid = true;
        const nomorKtp = $("#nomorKtp").val().trim();
        const namaLengkap = $("#namaLengkap").val().trim();
        const alamat = $("#alamat").val().trim();
        const tanggalLahir = $("#tanggalLahir").val();
        const jenisKelamin = $("#jenisKelamin").val();

        if (!/^\d{16}$/.test(nomorKtp)) {
            showFieldError("nomorKtp", "Nomor KTP harus tepat 16 digit angka");
            valid = false;
        }
        if (!namaLengkap) {
            showFieldError("namaLengkap", "Nama lengkap tidak boleh kosong");
            valid = false;
        }
        if (!alamat) {
            showFieldError("alamat", "Alamat tidak boleh kosong");
            valid = false;
        }
        if (!tanggalLahir) {
            showFieldError("tanggalLahir", "Tanggal lahir tidak boleh kosong");
            valid = false;
        }
        if (!jenisKelamin) {
            showFieldError("jenisKelamin", "Pilih jenis kelamin terlebih dahulu");
            valid = false;
        }
        return valid;
    }

    /** Collect form data into a plain object matching the API DTO */
    function getFormData() {
        return {
            nomorKtp: $("#nomorKtp").val().trim(),
            namaLengkap: $("#namaLengkap").val().trim(),
            alamat: $("#alamat").val().trim(),
            tanggalLahir: $("#tanggalLahir").val(),
            jenisKelamin: $("#jenisKelamin").val(),
        };
    }

    /** Reset form to "add" mode */
    function resetForm() {
        $("#ktp-form")[0].reset();
        $("#ktp-id").val("");
        clearFormErrors();
        $("#form-icon").text("➕");
        $("#form-heading-text").text("Tambah Data KTP");
        $("#btn-submit-icon").text("💾");
        $("#btn-submit-text").text("Simpan Data KTP");
        $("#btn-submit").removeClass("update-mode");
        setSubmitLoading(false);
        // Scroll form into view on mobile
        $(".form-panel")[0].scrollIntoView({ behavior: "smooth", block: "start" });
    }

    /** Toggle submit button loading state */
    function setSubmitLoading(loading) {
        const $btn = $("#btn-submit");
        if (loading) {
            $btn.prop("disabled", true);
            $("#btn-submit-icon, #btn-submit-text").addClass("hidden");
            $("#btn-submit-spinner").removeClass("hidden");
        } else {
            $btn.prop("disabled", false);
            $("#btn-submit-icon, #btn-submit-text").removeClass("hidden");
            $("#btn-submit-spinner").addClass("hidden");
        }
    }

    /** Update all stat counters */
    function updateStats(data) {
        const total = data.length;
        const lk = data.filter(k => k.jenisKelamin === "Laki-laki").length;
        const pr = data.filter(k => k.jenisKelamin === "Perempuan").length;
        $("#stat-total").text(total);
        $("#stat-lk").text(lk);
        $("#stat-pr").text(pr);
        $("#total-badge").text(`${total} Data`);
    }

    /* ============================================================
       RENDER TABLE
    ============================================================ */
    function renderTable(data) {
        const $tbody = $("#ktp-table-body");
        $tbody.empty();
        updateStats(data);

        if (!data || data.length === 0) {
            $("#empty-state").removeClass("hidden");
            return;
        }

        $("#empty-state").addClass("hidden");

        data.forEach((ktp, index) => {
            const genderBadge = ktp.jenisKelamin === "Laki-laki"
                ? `<span class="gender-badge gender-lk">♂ ${ktp.jenisKelamin}</span>`
                : `<span class="gender-badge gender-pr">♀ ${ktp.jenisKelamin}</span>`;

            const row = `
        <tr class="row-enter" id="row-${ktp.id}" data-nama="${escapeHtml(ktp.namaLengkap)}" data-nik="${escapeHtml(ktp.nomorKtp)}">
          <td>${index + 1}</td>
          <td><span class="nik-cell">${escapeHtml(ktp.nomorKtp)}</span></td>
          <td><strong>${escapeHtml(ktp.namaLengkap)}</strong></td>
          <td title="${escapeHtml(ktp.alamat)}">${escapeHtml(ktp.alamat)}</td>
          <td>${formatDate(ktp.tanggalLahir)}</td>
          <td>${genderBadge}</td>
          <td>
            <div class="action-cell">
              <button class="btn btn-edit" onclick="editKtp(${ktp.id})" title="Edit data KTP ini" aria-label="Edit KTP ${escapeHtml(ktp.namaLengkap)}">
                ✏️ Edit
              </button>
              <button class="btn btn-delete" onclick="deleteKtp(${ktp.id}, '${escapeHtml(ktp.namaLengkap)}')" title="Hapus data KTP ini" aria-label="Hapus KTP ${escapeHtml(ktp.namaLengkap)}">
                🗑️ Hapus
              </button>
            </div>
          </td>
        </tr>`;
            $tbody.append(row);
        });
    }

    /** Simple HTML escaping to prevent XSS */
    function escapeHtml(str) {
        if (!str) return "";
        return String(str)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    /* ============================================================
       API CALLS
    ============================================================ */

    /** GET /ktp — Load all KTP data */
    function loadAllKtp() {
        $("#ktp-table-body").html(`
      <tr id="loading-row">
        <td colspan="7" class="state-cell">
          <div class="loading-state">
            <div class="loading-spinner"></div>
            <span>Memuat data...</span>
          </div>
        </td>
      </tr>`);
        $("#empty-state").addClass("hidden");

        $.ajax({
            url: API_BASE,
            method: "GET",
            contentType: "application/json",
            success: function (response) {
                if (response.success) {
                    allKtpData = response.data || [];
                    filterAndRender();
                } else {
                    showToast(response.message || "Gagal memuat data.", "error");
                    renderTable([]);
                }
            },
            error: function (xhr) {
                const msg = xhr.responseJSON?.message || "Gagal terhubung ke server. Pastikan Spring Boot berjalan.";
                showToast(msg, "error");
                renderTable([]);
            },
        });
    }

    /** POST /ktp — Create new KTP */
    function createKtp(data) {
        setSubmitLoading(true);
        $.ajax({
            url: API_BASE,
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (response) {
                setSubmitLoading(false);
                if (response.success) {
                    showToast("✅ " + response.message, "success");
                    resetForm();
                    loadAllKtp();
                } else {
                    showToast(response.message || "Gagal menyimpan data.", "error");
                }
            },
            error: function (xhr) {
                setSubmitLoading(false);
                const errMsg = extractErrorMessage(xhr);
                showToast(errMsg, "error");
            },
        });
    }

    /** GET /ktp/{id} — Populate form with KTP data */
    window.editKtp = function (id) {
        showToast("Memuat data untuk diedit...", "info", 1500);
        $.ajax({
            url: `${API_BASE}/${id}`,
            method: "GET",
            contentType: "application/json",
            success: function (response) {
                if (response.success) {
                    const ktp = response.data;
                    // Populate form
                    $("#ktp-id").val(ktp.id);
                    $("#nomorKtp").val(ktp.nomorKtp);
                    $("#namaLengkap").val(ktp.namaLengkap);
                    $("#alamat").val(ktp.alamat);
                    $("#tanggalLahir").val(ktp.tanggalLahir);
                    $("#jenisKelamin").val(ktp.jenisKelamin);
                    clearFormErrors();

                    // Switch form to edit mode
                    $("#form-icon").text("✏️");
                    $("#form-heading-text").text("Edit Data KTP");
                    $("#btn-submit-icon").text("🔄");
                    $("#btn-submit-text").text("Perbarui Data KTP");

                    // Scroll to form
                    $(".form-panel")[0].scrollIntoView({ behavior: "smooth", block: "start" });
                    $("#nomorKtp").focus();
                } else {
                    showToast(response.message || "Data tidak ditemukan.", "error");
                }
            },
            error: function (xhr) {
                showToast(extractErrorMessage(xhr), "error");
            },
        });
    };

    /** PUT /ktp/{id} — Update existing KTP */
    function updateKtp(id, data) {
        setSubmitLoading(true);
        $.ajax({
            url: `${API_BASE}/${id}`,
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (response) {
                setSubmitLoading(false);
                if (response.success) {
                    showToast("🔄 " + response.message, "success");
                    resetForm();
                    loadAllKtp();
                } else {
                    showToast(response.message || "Gagal memperbarui data.", "error");
                }
            },
            error: function (xhr) {
                setSubmitLoading(false);
                showToast(extractErrorMessage(xhr), "error");
            },
        });
    }

    /** DELETE /ktp/{id} — Delete KTP */
    window.deleteKtp = function (id, nama) {
        pendingDeleteId = id;
        $("#modal-message").text(
            `Apakah Anda yakin ingin menghapus data KTP atas nama "${nama}"? Tindakan ini tidak dapat dibatalkan.`
        );
        $("#confirm-modal").removeClass("hidden");
        $("#modal-confirm").focus();
    };

    function confirmDelete(id) {
        $.ajax({
            url: `${API_BASE}/${id}`,
            method: "DELETE",
            contentType: "application/json",
            success: function (response) {
                if (response.success) {
                    showToast("🗑️ " + response.message, "success");
                    loadAllKtp();
                } else {
                    showToast(response.message || "Gagal menghapus data.", "error");
                }
            },
            error: function (xhr) {
                showToast(extractErrorMessage(xhr), "error");
            },
        });
    }

    /** Extract a readable error message from a jQuery XHR error */
    function extractErrorMessage(xhr) {
        if (!xhr.responseJSON) return "Gagal terhubung ke server. Pastikan API sedang berjalan.";
        const res = xhr.responseJSON;
        if (Array.isArray(res.data)) return res.data.join(", ");
        return res.message || "Terjadi kesalahan tidak terduga.";
    }

    /* ============================================================
       SEARCH / FILTER
    ============================================================ */
    function filterAndRender() {
        const q = $("#search-input").val().toLowerCase().trim();
        if (!q) {
            renderTable(allKtpData);
            return;
        }
        const filtered = allKtpData.filter(k =>
            k.namaLengkap.toLowerCase().includes(q) ||
            k.nomorKtp.toLowerCase().includes(q) ||
            k.alamat.toLowerCase().includes(q)
        );
        renderTable(filtered);
    }

    $("#search-input").on("input", filterAndRender);

    /* ============================================================
       FORM SUBMIT
    ============================================================ */
    $("#ktp-form").on("submit", function (e) {
        e.preventDefault();

        if (!validateForm()) {
            showToast("Harap isi semua field dengan benar.", "warning");
            return;
        }

        const id = $("#ktp-id").val();
        const data = getFormData();

        if (id) {
            updateKtp(parseInt(id), data);
        } else {
            createKtp(data);
        }
    });

    /* ============================================================
       BUTTON EVENTS
    ============================================================ */

    // Reset button
    $("#btn-reset").on("click", resetForm);

    // Refresh button
    $("#btn-refresh").on("click", function () {
        showToast("Memuat ulang data...", "info", 1200);
        loadAllKtp();
    });

    // Modal cancel
    $("#modal-cancel").on("click", function () {
        $("#confirm-modal").addClass("hidden");
        pendingDeleteId = null;
    });

    // Modal confirm
    $("#modal-confirm").on("click", function () {
        if (pendingDeleteId != null) {
            confirmDelete(pendingDeleteId);
            pendingDeleteId = null;
        }
        $("#confirm-modal").addClass("hidden");
    });

    // Close modal clicking backdrop
    $("#confirm-modal").on("click", function (e) {
        if ($(e.target).is("#confirm-modal")) {
            $("#confirm-modal").addClass("hidden");
            pendingDeleteId = null;
        }
    });

    // Keyboard accessibility for modal
    $(document).on("keydown", function (e) {
        if (e.key === "Escape" && !$("#confirm-modal").hasClass("hidden")) {
            $("#confirm-modal").addClass("hidden");
            pendingDeleteId = null;
        }
    });

    // Only allow digits in NIK field
    $("#nomorKtp").on("input", function () {
        const val = $(this).val().replace(/\D/g, "").slice(0, 16);
        $(this).val(val);
    });
});
