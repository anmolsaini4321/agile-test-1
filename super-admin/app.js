/**
 * CREWHQ - SUPER ADMIN PORTAL STATE & CONTROLLER LOGIC
 */

document.addEventListener('DOMContentLoaded', () => {
    const API_BASE_URL = 'https://agile-test-1-backend-second-branch.onrender.com';

    // Initial Mock Data (used if LocalStorage is empty)
    const initialAdmins = [
        {
            id: "adm_01",
            name: "Vikash Gorai",
            email: "vikashgorai985276@gmail.com",
            companyName: "Wickcore Systems",
            companyCode: "WCK90",
            status: "APPROVED", // APPROVED, PENDING, SUSPENDED
            allowedFeatures: ["ATTENDANCE", "CHAT"],
            createdAt: "2026-06-28 14:32"
        },
        {
            id: "adm_02",
            name: "Anmol Sharma",
            email: "anmol.sharma@smarthr.io",
            companyName: "SmartHR Inc",
            companyCode: "SMT24",
            status: "PENDING",
            allowedFeatures: ["ATTENDANCE", "LEAVES", "TASKS", "MEETINGS", "CHAT"],
            createdAt: "2026-07-03 10:15"
        },
        {
            id: "adm_03",
            name: "Ramesh Kumar",
            email: "ramesh.kumar@techsolutions.com",
            companyName: "TechSolutions Co",
            companyCode: "TEC88",
            status: "APPROVED",
            allowedFeatures: ["ATTENDANCE", "LEAVES", "TASKS"],
            createdAt: "2026-06-15 09:00"
        },
        {
            id: "adm_04",
            name: "Priya Singh",
            email: "priya.singh@hrcorp.org",
            companyName: "HRCorp Group",
            companyCode: "HRC10",
            status: "SUSPENDED",
            allowedFeatures: ["CHAT"],
            createdAt: "2026-05-20 11:45"
        },
        {
            id: "adm_05",
            name: "Rahul Verma",
            email: "rahul.verma@acme.com",
            companyName: "Acme Industries",
            companyCode: "ACM12",
            status: "PENDING",
            allowedFeatures: ["ATTENDANCE", "LEAVES"],
            createdAt: "2026-07-04 08:30"
        }
    ];

    // Mock Database of Employees mapped by Company Code
    const mockEmployees = {
        WCK90: [
            {
                id: "emp_wck01",
                name: "Amit Sen",
                email: "amit.sen@wickcore.com",
                designation: "Senior Lead Architect",
                department: "Engineering",
                gender: "Male",
                maritalStatus: "Married",
                bloodGroup: "A+",
                physicallyChallenged: "No",
                currentAddress: "12, Outer Ring Road, Bellandur, Bangalore, Karnataka - 560103",
                permanentAddress: "Sector 4, Salt Lake, Kolkata, West Bengal - 700091",
                fathersName: "Bimal Sen",
                mothersName: "Kakoli Sen",
                emergencyName: "Kakoli Sen",
                emergencyRelation: "Mother",
                emergencyNumber: "+91 9830012345",
                bankName: "State Bank of India",
                accountHolder: "Amit Sen",
                accountNumber: "30512345678",
                ifscCode: "SBIN0001042",
                upiId: "amitsen@upisbi",
                uan: "100908070605",
                pan: "APQPS1029D",
                pfNumber: "WB/CAL/10090/654",
                pfJoining: "01 July 2024",
                esiNumber: "31090807060501234",
                esiJoining: "01 July 2024",
                epsNumber: "EPS880923",
                epsExit: "Active",
                avatarUrl: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=128"
            },
            {
                id: "emp_wck02",
                name: "Sneha Rao",
                email: "sneha.rao@wickcore.com",
                designation: "QA Engineer",
                department: "Quality Assurance",
                gender: "Female",
                maritalStatus: "Single",
                bloodGroup: "O+",
                physicallyChallenged: "No",
                currentAddress: "Rose Apartments, Marathahalli, Bangalore, Karnataka - 560037",
                permanentAddress: "JP Nagar, Mysore, Karnataka - 570008",
                fathersName: "Govinda Rao",
                mothersName: "Sumati Rao",
                emergencyName: "Govinda Rao",
                emergencyRelation: "Father",
                emergencyNumber: "+91 9845098765",
                bankName: "HDFC Bank Ltd",
                accountHolder: "Sneha Rao",
                accountNumber: "50100439281726",
                ifscCode: "HDFC0000184",
                upiId: "sneha.rao@okhdfc",
                uan: "100807060504",
                pan: "BGPPR9876G",
                pfNumber: "KN/MY/10080/776",
                pfJoining: "12 Oct 2025",
                esiNumber: "31180706050499882",
                esiJoining: "12 Oct 2025",
                epsNumber: "EPS7765439",
                epsExit: "Active",
                avatarUrl: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&q=80&w=128"
            }
        ],
        SMT24: [
            {
                id: "emp_smt01",
                name: "Pooja Saini",
                email: "pooja.saini@smarthr.io",
                designation: "UI/UX Designer",
                department: "Design & UX",
                gender: "Female",
                maritalStatus: "Single",
                bloodGroup: "B+",
                physicallyChallenged: "No",
                currentAddress: "Sector 2, HSR Layout, Bangalore, Karnataka - 560102",
                permanentAddress: "Model Town, Panipat, Haryana - 132103",
                fathersName: "Jasbir Saini",
                mothersName: "Raj Kumari",
                emergencyName: "Jasbir Saini",
                emergencyRelation: "Father",
                emergencyNumber: "+91 9416012345",
                bankName: "ICICI Bank Ltd",
                accountHolder: "Pooja Saini",
                accountNumber: "000701982736",
                ifscCode: "ICIC0000007",
                upiId: "poojasaini@okicici",
                uan: "101211100908",
                pan: "CPSPS9922K",
                pfNumber: "HR/PNP/10121/992",
                pfJoining: "15 April 2025",
                esiNumber: "31221110090812345",
                esiJoining: "15 April 2025",
                epsNumber: "EPS992211",
                epsExit: "Active",
                avatarUrl: "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&q=80&w=128"
            },
            {
                id: "emp_smt02",
                name: "Rohan Gupta",
                email: "rohan.gupta@smarthr.io",
                designation: "Mobile App Engineer",
                department: "Engineering",
                gender: "Male",
                maritalStatus: "Single",
                bloodGroup: "O-",
                physicallyChallenged: "No",
                currentAddress: "Green Glen Layout, Bellandur, Bangalore, Karnataka - 560103",
                permanentAddress: "Sector 15, Rohini, New Delhi - 110085",
                fathersName: "Vijay Gupta",
                mothersName: "Sunita Gupta",
                emergencyName: "Vijay Gupta",
                emergencyRelation: "Father",
                emergencyNumber: "+91 9876543210",
                bankName: "HDFC Bank Ltd",
                accountHolder: "Rohan Gupta",
                accountNumber: "50100432109876",
                ifscCode: "HDFC0000123",
                upiId: "rohan@okhdfcbank",
                uan: "100987654321",
                pan: "BGPPR1234K",
                pfNumber: "KN/BG/12345/678",
                pfJoining: "02 July 2026",
                esiNumber: "31123456789012345",
                esiJoining: "02 July 2026",
                epsNumber: "EPS9876543",
                epsExit: "Active",
                avatarUrl: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&q=80&w=128"
            }
        ],
        TEC88: [
            {
                id: "emp_tec01",
                name: "Karan Mehra",
                email: "karan.mehra@techsolutions.com",
                designation: "DevOps Engineer",
                department: "Infrastructure",
                gender: "Male",
                maritalStatus: "Married",
                bloodGroup: "AB+",
                physicallyChallenged: "No",
                currentAddress: "Elite Homes, Whitefield, Bangalore, Karnataka - 560066",
                permanentAddress: "Sukhdev Vihar, Okhla, New Delhi - 110025",
                fathersName: "Satish Mehra",
                mothersName: "Ritu Mehra",
                emergencyName: "Ritu Mehra",
                emergencyRelation: "Mother",
                emergencyNumber: "+91 9811099887",
                bankName: "Axis Bank Ltd",
                accountHolder: "Karan Mehra",
                accountNumber: "915010029381726",
                ifscCode: "UTIB0000015",
                upiId: "karanmehra@okaxis",
                uan: "100293817263",
                pan: "CDPPM0293L",
                pfNumber: "DL/OKH/10029/932",
                pfJoining: "10 Jan 2023",
                esiNumber: "31029381726311223",
                esiJoining: "10 Jan 2023",
                epsNumber: "EPS029381",
                epsExit: "Active",
                avatarUrl: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&q=80&w=128"
            }
        ],
        HRC10: [
            {
                id: "emp_hrc01",
                name: "Rahul Verma",
                email: "rahul.verma@hrcorp.org",
                designation: "HR Specialist",
                department: "Recruitment",
                gender: "Male",
                maritalStatus: "Single",
                bloodGroup: "O+",
                physicallyChallenged: "No",
                currentAddress: "Electronic City, Bangalore, Karnataka",
                permanentAddress: "Indore, Madhya Pradesh",
                fathersName: "Pradeep Verma",
                mothersName: "Nirmala Verma",
                emergencyName: "Pradeep Verma",
                emergencyRelation: "Father",
                emergencyNumber: "+91 9988776655",
                bankName: "Punjab National Bank",
                accountHolder: "Rahul Verma",
                accountNumber: "40192837465",
                ifscCode: "PUNB0109283",
                upiId: "rahulverma@okpnb",
                uan: "100492837465",
                pan: "CGPPR4928D",
                pfNumber: "MP/IND/10049/223",
                pfJoining: "18 Nov 2024",
                esiNumber: "31049283746599881",
                esiJoining: "18 Nov 2024",
                epsNumber: "EPS223940",
                epsExit: "Active",
                avatarUrl: "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?auto=format&fit=crop&q=80&w=128"
            }
        ],
        ACM12: [
            {
                id: "emp_acm01",
                name: "Preeti Sinha",
                email: "preeti.sinha@acme.com",
                designation: "Operations Analyst",
                department: "Operations",
                gender: "Female",
                maritalStatus: "Married",
                bloodGroup: "A-",
                physicallyChallenged: "No",
                currentAddress: "Whitefield, Bangalore, Karnataka",
                permanentAddress: "Kankarbagh, Patna, Bihar",
                fathersName: "Raman Sinha",
                mothersName: "Anjali Sinha",
                emergencyName: "Anjali Sinha",
                emergencyRelation: "Mother",
                emergencyNumber: "+91 9304012345",
                bankName: "Bank of Baroda",
                accountHolder: "Preeti Sinha",
                accountNumber: "1029384756",
                ifscCode: "BARB0WHITEF",
                upiId: "preetisinha@okbaroda",
                uan: "100102938475",
                pan: "DGPPS1029E",
                pfNumber: "BR/PAT/10010/884",
                pfJoining: "05 May 2025",
                esiNumber: "31010293847599002",
                esiJoining: "05 May 2025",
                epsNumber: "EPS884920",
                epsExit: "Active",
                avatarUrl: "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&q=80&w=128"
            }
        ]
    };

    // State Variables
    let admins = [];
    let currentTab = 'all'; // all, pending, approved
    let searchQuery = '';
    let selectedFeatureFilter = 'all';
    let activeEditAdminId = null;
    let activeView = 'dashboard'; // dashboard, directory
    let selectedCompanyCode = null;

    // Trail Map State variables
    let trailMap = null;
    let trailPolyline = null;
    let trailMarkers = [];

    // Live Tracking State
    let stompClient = null;
    let liveTrackingActive = false;
    let liveEmployeeId = null;
    let liveEmployeeData = null;
    let liveMarker = null;
    let livePolylinePoints = [];
    let livePointCount = 0;
    let liveSubscription = null;


    // DOM Elements
    const adminsTableBody = document.getElementById('admins-table-body');
    const tableEmptyState = document.getElementById('table-empty-state');
    const currentDateEl = document.getElementById('current-date');
    const searchInput = document.getElementById('admin-search-input');
    const featureFilter = document.getElementById('feature-filter');
    const tabLinks = document.querySelectorAll('.tab-link');
    const mainViewTitle = document.querySelector('.header-title h1');
    const sidebarMenuItems = document.querySelectorAll('.sidebar-menu .menu-item');
    const contentSections = document.querySelectorAll('.content-section');
    
    // Stats Elements
    const statTotalAdmins = document.getElementById('stat-total-admins');
    const statPendingApprovals = document.getElementById('stat-pending-approvals');
    const statActiveCompanies = document.getElementById('stat-active-companies');
    const badgePendingCount = document.getElementById('badge-pending-count');
    
    // License Config Modal Elements
    const featureModal = document.getElementById('feature-modal');
    const btnCloseModal = document.getElementById('btn-close-modal');
    const btnCancelModal = document.getElementById('btn-cancel-modal');
    const btnSaveFeatures = document.getElementById('btn-save-features');
    const modalAdminName = document.getElementById('modal-admin-name');
    const modalAdminEmail = document.getElementById('modal-admin-email');
    const modalAdminCompany = document.getElementById('modal-admin-company');

    // Directory View Elements
    const dirCompanyList = document.getElementById('dir-company-list');
    const dirSelectPrompt = document.getElementById('dir-select-prompt');
    const dirDetailsContent = document.getElementById('dir-details-content');
    
    const dirAdminAvatar = document.getElementById('dir-admin-avatar');
    const dirAdminName = document.getElementById('dir-admin-name');
    const dirAdminEmail = document.getElementById('dir-admin-email');
    const dirAdminCompany = document.getElementById('dir-admin-company');
    const dirAdminCode = document.getElementById('dir-admin-code');
    const dirAdminStatus = document.getElementById('dir-admin-status');
    const dirAdminFeatures = document.getElementById('dir-admin-features');
    const dirEmployeeCount = document.getElementById('dir-employee-count');
    const dirEmployeesListBody = document.getElementById('dir-employees-list-body');

    // Employee Details Modal Elements
    const employeeModal = document.getElementById('employee-modal');
    const btnCloseEmpModal = document.getElementById('btn-close-emp-modal');
    const btnCloseEmpModalFooter = document.getElementById('btn-close-emp-modal-footer');
    
    const empModalAvatar = document.getElementById('emp-modal-avatar');
    const empModalName = document.getElementById('emp-modal-name');
    const empModalDesignation = document.getElementById('emp-modal-designation');
    const empModalDeptBadge = document.getElementById('emp-modal-dept-badge');
    const empModalCodeBadge = document.getElementById('emp-modal-code-badge');
    
    const empModalGender = document.getElementById('emp-modal-gender');
    const empModalMarital = document.getElementById('emp-modal-marital');
    const empModalBlood = document.getElementById('emp-modal-blood');
    const empModalHandicapped = document.getElementById('emp-modal-handicapped');
    const empModalCurrentAddress = document.getElementById('emp-modal-current-address');
    const empModalPermanentAddress = document.getElementById('emp-modal-permanent-address');
    
    const empModalFather = document.getElementById('emp-modal-father');
    const empModalMother = document.getElementById('emp-modal-mother');
    const empModalEmergencyName = document.getElementById('emp-modal-emergency-name');
    const empModalEmergencyRelation = document.getElementById('emp-modal-emergency-relation');
    const empModalEmergencyPhone = document.getElementById('emp-modal-emergency-phone');
    
    const empModalBankName = document.getElementById('emp-modal-bank-name');
    const empModalBankHolder = document.getElementById('emp-modal-bank-holder');
    const empModalBankNumber = document.getElementById('emp-modal-bank-number');
    const empModalBankIfsc = document.getElementById('emp-modal-bank-ifsc');
    const empModalBankUpi = document.getElementById('emp-modal-bank-upi');
    
    const empModalUan = document.getElementById('emp-modal-uan');
    const empModalPan = document.getElementById('emp-modal-pan');
    const empModalPf = document.getElementById('emp-modal-pf');
    const empModalPfDate = document.getElementById('emp-modal-pf-date');
    const empModalEsi = document.getElementById('emp-modal-esi');
    const empModalEsiDate = document.getElementById('emp-modal-esi-date');
    const empModalEps = document.getElementById('emp-modal-eps');
    const empModalEpsExit = document.getElementById('emp-modal-eps-exit');
    
    // Feature Checkboxes in Modal
    const featureCheckboxes = {
        ATTENDANCE: document.getElementById('chk-attendance'),
        LEAVES: document.getElementById('chk-leaves'),
        TASKS: document.getElementById('chk-tasks'),
        MEETINGS: document.getElementById('chk-meetings'),
        CHAT: document.getElementById('chk-chat')
    };

    // New Request Mock Generator Button
    const btnAddMockAdmin = document.getElementById('btn-add-mock-admin');
    let attendanceViewMode = 'daily';

    // 1. Initialize Application State
    async function init() {
        // Set date
        const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        currentDateEl.textContent = new Date().toLocaleDateString('en-US', options);

        // Load data from live backend with fallback
        try {
            const response = await fetch(`${API_BASE_URL}/super-admin/companies`);
            if (response.ok) {
                admins = await response.json();
                console.log("Successfully loaded companies from live database:", admins);
            } else {
                throw new Error("HTTP error: " + response.status);
            }
        } catch (e) {
            console.warn("Backend unreachable, falling back to local storage:", e.message);
            const savedAdmins = localStorage.getItem('crewHQ_admins');
            if (savedAdmins) {
                admins = JSON.parse(savedAdmins);
                showToast("⚠️ Backend offline — showing cached data. Changes won't persist.", "warning");
            } else {
                admins = initialAdmins;
                saveState();
                showToast("⚠️ Backend offline — showing demo data. Start the backend server.", "warning");
            }
        }

        switchView(activeView);
        setupEventListeners();
        showToast("Super Admin dashboard loaded successfully.", "success");
    }

    // 2. Save State to LocalStorage
    function saveState() {
        localStorage.setItem('crewHQ_admins', JSON.stringify(admins));
    }

    // 3. View Switcher Routing
    function switchView(viewName) {
        activeView = viewName;

        // Toggle menu active class
        sidebarMenuItems.forEach(item => {
            if (item.getAttribute('data-section') === viewName) {
                item.classList.add('active');
            } else {
                item.classList.remove('active');
            }
        });

        // Toggle layout visibility
        contentSections.forEach(section => {
            if (section.id === `section-${viewName}`) {
                section.classList.remove('hidden');
            } else {
                section.classList.add('hidden');
            }
        });

        // Load correct rendering view
        if (viewName === 'dashboard') {
            mainViewTitle.textContent = "Dashboard Overview";
            renderDashboard();
        } else if (viewName === 'directory') {
            mainViewTitle.textContent = "Company Directory";
            renderDirectoryView();
        } else if (viewName === 'trail') {
            mainViewTitle.textContent = "Employee Location Trails";
            populateTrailCompanies();
        } else if (viewName === 'attendance') {
            mainViewTitle.textContent = "Attendance Monitor";
            populateAttendanceCompanies();
        }
    }

    // 4. Render Table and Dashboard Metrics
    function renderDashboard() {
        // Calculate Metrics
        const totalCount = admins.length;
        const pendingCount = admins.filter(a => a.status === 'PENDING').length;
        const activeCompaniesCount = admins.filter(a => a.status === 'APPROVED').length;

        // Render metrics to UI
        statTotalAdmins.textContent = totalCount;
        statPendingApprovals.textContent = pendingCount;
        statActiveCompanies.textContent = activeCompaniesCount;
        badgePendingCount.textContent = pendingCount;

        // Filter and Search admins
        const filteredAdmins = admins.filter(admin => {
            // Tab filter
            if (currentTab === 'pending' && admin.status !== 'PENDING') return false;
            if (currentTab === 'approved' && admin.status !== 'APPROVED') return false;

            // Feature license filter
            if (selectedFeatureFilter !== 'all' && !admin.allowedFeatures.includes(selectedFeatureFilter)) return false;

            // Search query match
            if (searchQuery) {
                const query = searchQuery.toLowerCase();
                const matchesName = admin.name.toLowerCase().includes(query);
                const matchesEmail = admin.email.toLowerCase().includes(query);
                const matchesCompany = admin.companyName.toLowerCase().includes(query) || admin.companyCode.toLowerCase().includes(query);
                return matchesName || matchesEmail || matchesCompany;
            }

            return true;
        });

        // Populate Table Body
        adminsTableBody.innerHTML = '';
        
        if (filteredAdmins.length === 0) {
            tableEmptyState.classList.remove('hidden');
            return;
        }
        
        tableEmptyState.classList.add('hidden');

        filteredAdmins.forEach(admin => {
            const tr = document.createElement('tr');
            
            // Info Column HTML
            const infoHTML = `
                <div class="admin-info-cell">
                    <img class="admin-avatar" src="${getAvatarUrl(admin.name)}" alt="${admin.name}">
                    <div class="admin-name-wrapper">
                        <h4>${admin.name}</h4>
                        <span class="reg-date">Joined: ${admin.createdAt}</span>
                    </div>
                </div>
            `;

            // Status Column HTML
            let statusClass = 'pending';
            if (admin.status === 'APPROVED') statusClass = 'approved';
            if (admin.status === 'SUSPENDED') statusClass = 'suspended';
            
            const statusHTML = `
                <span class="badge-status ${statusClass}">
                    <span class="status-dot"></span>
                    ${admin.status}
                </span>
            `;

            // Features Column HTML
            const features = ["ATTENDANCE", "LEAVES", "TASKS", "MEETINGS", "CHAT"];
            const featuresHTML = `
                <div class="features-list">
                    ${features.map(f => {
                        const active = admin.allowedFeatures.includes(f);
                        const icon = getFeatureIcon(f);
                        return `
                            <span class="feature-tag ${active ? 'active' : 'inactive'}" title="${f}">
                                <span class="material-symbols-outlined">${icon}</span>
                                ${f.charAt(0) + f.slice(1).toLowerCase()}
                            </span>
                        `;
                    }).join('')}
                </div>
            `;

            // Actions Column HTML
            let actionButtons = '';
            if (admin.status === 'PENDING') {
                actionButtons = `
                    <button class="btn btn-secondary btn-sm btn-approve" data-id="${admin.id}">
                        <span class="material-symbols-outlined">check_circle</span>
                        Approve
                    </button>
                    <button class="btn btn-secondary btn-sm btn-suspend" data-id="${admin.id}" title="Reject Request">
                        <span class="material-symbols-outlined">cancel</span>
                    </button>
                `;
            } else if (admin.status === 'APPROVED') {
                actionButtons = `
                    <button class="btn btn-secondary btn-sm btn-edit-features" data-id="${admin.id}">
                        <span class="material-symbols-outlined">tune</span>
                        Features
                    </button>
                    <button class="btn btn-secondary btn-sm btn-suspend" data-id="${admin.id}" title="Suspend Account">
                        <span class="material-symbols-outlined">block</span>
                    </button>
                `;
            } else {
                // Suspended
                actionButtons = `
                    <button class="btn btn-secondary btn-sm btn-approve" data-id="${admin.id}">
                        <span class="material-symbols-outlined">check</span>
                        Activate
                    </button>
                `;
            }

            tr.innerHTML = `
                <td>${infoHTML}</td>
                <td>${admin.email}</td>
                <td>
                    <div class="company-cell">
                        <div>${admin.companyName}</div>
                        <div class="company-code">${admin.companyCode}</div>
                    </div>
                </td>
                <td>${statusHTML}</td>
                <td>${featuresHTML}</td>
                <td>
                    <div class="actions-cell">
                        ${actionButtons}
                    </div>
                </td>
            `;

            adminsTableBody.appendChild(tr);
        });

        // Wire Up Action Button Events inside table
        document.querySelectorAll('.btn-approve').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const id = e.currentTarget.getAttribute('data-id');
                approveAdmin(id);
            });
        });

        document.querySelectorAll('.btn-suspend').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const id = e.currentTarget.getAttribute('data-id');
                suspendAdmin(id);
            });
        });

        document.querySelectorAll('.btn-edit-features').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const id = e.currentTarget.getAttribute('data-id');
                openFeatureModal(id);
            });
        });
    }

    // 5. Render Company & Employees Directory view
    function renderDirectoryView() {
        // Clear sidebar
        dirCompanyList.innerHTML = '';

        // Load all companies (approved or suspended)
        const companies = admins.filter(a => a.status === 'APPROVED' || a.status === 'SUSPENDED');

        if (companies.length === 0) {
            dirCompanyList.innerHTML = `<div style="padding:20px; font-size:12px; color:var(--text-muted); text-align:center;">No approved companies found.</div>`;
            dirSelectPrompt.classList.remove('hidden');
            dirDetailsContent.classList.add('hidden');
            return;
        }

        companies.forEach(company => {
            const btn = document.createElement('button');
            btn.className = `company-list-item ${selectedCompanyCode === company.companyCode ? 'active' : ''}`;
            btn.setAttribute('data-code', company.companyCode);
            
            btn.innerHTML = `
                <span class="material-symbols-outlined">domain</span>
                <div class="comp-meta">
                    <h4>${company.companyName}</h4>
                    <p>Code: ${company.companyCode}</p>
                </div>
            `;

            btn.onclick = () => {
                document.querySelectorAll('.company-list-item').forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                selectedCompanyCode = company.companyCode;
                loadCompanyDetails(company);
            };

            dirCompanyList.appendChild(btn);
        });

        // Re-load currently selected company if available
        if (selectedCompanyCode) {
            const activeCompany = companies.find(c => c.companyCode === selectedCompanyCode);
            if (activeCompany) {
                loadCompanyDetails(activeCompany);
            } else {
                selectedCompanyCode = null;
                dirSelectPrompt.classList.remove('hidden');
                dirDetailsContent.classList.add('hidden');
            }
        } else {
            dirSelectPrompt.classList.remove('hidden');
            dirDetailsContent.classList.add('hidden');
        }
    }

    async function loadCompanyDetails(company) {
        dirSelectPrompt.classList.add('hidden');
        dirDetailsContent.classList.remove('hidden');

        // Admin info - live data uses adminName/adminEmail (from SuperAdminCompanyDto)
        const adminName = company.adminName || company.name || 'Admin';
        const adminEmail = company.adminEmail || company.email || 'N/A';
        dirAdminAvatar.src = getAvatarUrl(adminName);
        dirAdminName.textContent = adminName;
        dirAdminEmail.textContent = adminEmail;
        dirAdminCompany.textContent = company.companyName;
        dirAdminCode.textContent = company.companyCode;
        
        dirAdminStatus.textContent = company.status;
        dirAdminStatus.className = `badge-status ${company.status ? company.status.toLowerCase() : 'approved'}`;

        const dirStatusSelect = document.getElementById('dir-status-select');
        if (dirStatusSelect) {
            dirStatusSelect.value = company.status || 'APPROVED';
            dirStatusSelect.onchange = async (e) => {
                const newStatus = e.target.value;
                try {
                    const response = await fetch(`${API_BASE_URL}/super-admin/companies/${company.id}/status?status=${newStatus}`, {
                        method: 'POST'
                    });
                    if (response.ok) {
                        company.status = newStatus;
                        dirAdminStatus.textContent = newStatus;
                        dirAdminStatus.className = `badge-status ${newStatus.toLowerCase()}`;
                        showToast(`Status updated to ${newStatus} successfully.`, 'success');
                        
                        const cachedAdmin = admins.find(a => a.id === company.id);
                        if (cachedAdmin) cachedAdmin.status = newStatus;
                        
                        renderDashboard();
                    } else {
                        throw new Error("HTTP " + response.status);
                    }
                } catch (err) {
                    console.error("Failed to update company status", err);
                    showToast("Failed to update status on server.", "danger");
                    dirStatusSelect.value = company.status || 'APPROVED';
                }
            };
        }

        const dirAdminPhone = document.getElementById('dir-admin-phone');
        const dirAdminDept = document.getElementById('dir-admin-dept');
        if (dirAdminPhone) dirAdminPhone.textContent = 'Loading...';
        if (dirAdminDept) dirAdminDept.textContent = 'Loading...';

        if (company.adminId) {
            fetch(`${API_BASE_URL}/users/${company.adminId}`)
                .then(res => res.ok ? res.json() : null)
                .then(adminUser => {
                    if (adminUser) {
                        if (dirAdminPhone) dirAdminPhone.textContent = adminUser.phone || 'N/A';
                        if (dirAdminDept) {
                            const dept = adminUser.department || '';
                            const pos = adminUser.position || '';
                            dirAdminDept.textContent = `${pos.replace(/_/g, ' ') || 'HR/Admin'} (${dept.replace(/_/g, ' ') || 'Management'})`;
                        }
                    } else {
                        if (dirAdminPhone) dirAdminPhone.textContent = 'N/A';
                        if (dirAdminDept) dirAdminDept.textContent = 'HR/Admin';
                    }
                })
                .catch(err => {
                    console.error("Failed to fetch admin details", err);
                    if (dirAdminPhone) dirAdminPhone.textContent = 'N/A';
                    if (dirAdminDept) dirAdminDept.textContent = 'HR/Admin';
                });
        } else {
            if (dirAdminPhone) dirAdminPhone.textContent = 'N/A';
            if (dirAdminDept) dirAdminDept.textContent = 'HR/Admin';
        }

        const btnViewAdminProfile = document.getElementById('btn-view-admin-profile');
        if (btnViewAdminProfile) {
            btnViewAdminProfile.onclick = () => {
                openEmployeeProfileModal({
                    id: company.adminId || `admin_${company.companyCode}`,
                    name: adminName,
                    email: adminEmail,
                    role: "ROLE_ADMIN"
                }, company.companyCode);
            };
        }

        // Admin features tags
        const features = ["ATTENDANCE", "LEAVES", "TASKS", "MEETINGS", "CHAT"];
        const allowedFeatures = company.allowedFeatures || features;
        dirAdminFeatures.innerHTML = features.map(f => {
            const active = allowedFeatures.includes(f);
            return `<span class="feature-tag ${active ? 'active' : 'inactive'}">
                <span class="material-symbols-outlined" style="font-size:12px;">${getFeatureIcon(f)}</span>
                ${f.charAt(0) + f.slice(1).toLowerCase()}
            </span>`;
        }).join('');

        // Employees list - fetch from live backend
        dirEmployeesListBody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding: 20px; color:var(--text-muted); font-size:13px;">Loading employees...</td></tr>`;

        let employees = [];
        try {
            const response = await fetch(`${API_BASE_URL}/companies/code/${company.companyCode}/employees`);
            if (response.ok) {
                employees = await response.json();
            } else {
                throw new Error("HTTP " + response.status);
            }
        } catch (e) {
            console.log("Falling back to mock employees:", e.message);
            employees = mockEmployees[company.companyCode] || [];
        }

        dirEmployeeCount.textContent = `${employees.length} employee${employees.length === 1 ? '' : 's'}`;
        dirEmployeesListBody.innerHTML = '';

        if (employees.length === 0) {
            dirEmployeesListBody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding: 30px; color:var(--text-muted); font-size:13px;">No employee records found for this company.</td></tr>`;
            return;
        }

        employees.forEach(emp => {
            const tr = document.createElement('tr');
            const avatarSrc = emp.imageUrl || getAvatarUrl(emp.name || 'Employee');
            const designation = emp.position || emp.designation || 'Employee';
            const department = emp.department || '-';
            
            tr.innerHTML = `
                <td>
                    <div class="dir-emp-name-cell">
                        <img src="${avatarSrc}" alt="${emp.name}" class="dir-emp-avatar" onerror="this.src='${getAvatarUrl(emp.name || 'E')}'">
                        <strong>${emp.name || 'Unknown'}</strong>
                    </div>
                </td>
                <td>${emp.email || '-'}</td>
                <td>
                    <div class="dir-emp-role-dept">
                        <span class="role">${designation}</span>
                        <span class="dept">${department}</span>
                    </div>
                </td>
                <td class="text-right" style="display:flex; gap:6px; justify-content:flex-end;">
                    <button class="btn btn-primary btn-sm btn-view-emp-profile" data-id="${emp.id}" style="display: flex; align-items: center; gap: 5px;">
                        <span class="material-symbols-outlined">person</span> Profile
                    </button>
                    <button class="btn btn-secondary btn-sm btn-view-trail-emp" data-id="${emp.id}" data-code="${company.companyCode}" style="display: flex; align-items: center; gap: 5px;">
                        <span class="material-symbols-outlined">route</span> Trail
                    </button>
                </td>
            `;

            tr.querySelector('.btn-view-emp-profile').onclick = () => {
                openEmployeeProfileModal(emp, company.companyCode);
            };

            tr.querySelector('.btn-view-trail-emp').onclick = () => {
                switchView('trail');
                setTimeout(() => {
                    const companySelect = document.getElementById('trail-company-select');
                    if (companySelect) {
                        companySelect.value = company.companyCode;
                        companySelect.dispatchEvent(new Event('change'));
                        setTimeout(() => {
                            const empSelect = document.getElementById('trail-employee-select');
                            if (empSelect) {
                                empSelect.value = emp.id;
                            }
                            const dateInput = document.getElementById('trail-date-select');
                            if (dateInput && !dateInput.value) {
                                dateInput.value = new Date().toISOString().split('T')[0];
                            }
                        }, 1000);
                    }
                }, 300);
            };

            dirEmployeesListBody.appendChild(tr);
        });

    }

    // 6. Action Handlers
    async function approveAdmin(id) {
        const adminIndex = admins.findIndex(a => a.id === id);
        if (adminIndex > -1) {
            admins[adminIndex].status = 'APPROVED';
            saveState();

            try {
                await fetch(`${API_BASE_URL}/super-admin/companies/${id}/status?status=APPROVED`, {
                    method: 'POST'
                });
            } catch (e) {
                console.log("Could not update status on backend:", e.message);
            }
            
            // Generate mock employees if they don't exist yet for this code
            const code = admins[adminIndex].companyCode;
            if (!mockEmployees[code]) {
                const safeName = admins[adminIndex].name || "Admin";
                const safeEmail = admins[adminIndex].email || "admin@company.com";
                mockEmployees[code] = [
                    {
                        id: `emp_${code}_01`,
                        name: `${safeName.split(' ')[0]}'s Employee 1`,
                        email: `staff1@${safeEmail.split('@')[1]}`,
                        designation: "Software Engineer",
                        department: "Technology",
                        gender: "Male",
                        maritalStatus: "Single",
                        bloodGroup: "O+",
                        physicallyChallenged: "No",
                        currentAddress: "Prestige Shantiniketan, Whitefield, Bangalore",
                        permanentAddress: "Sector 10, Noida, UP",
                        fathersName: "Rameshwar Prasad",
                        mothersName: "Kanti Devi",
                        emergencyName: "Rameshwar Prasad",
                        emergencyRelation: "Father",
                        emergencyNumber: "+91 9990001112",
                        bankName: "State Bank of India",
                        accountHolder: "Employee 1",
                        accountNumber: "20198273645",
                        ifscCode: "SBIN0001827",
                        upiId: "employee1@upisbi",
                        uan: "100918273645",
                        pan: "BGPPR2918D",
                        pfNumber: "UP/NOI/10091/928",
                        pfJoining: "04 July 2026",
                        esiNumber: "31091827364599812",
                        esiJoining: "04 July 2026",
                        epsNumber: "EPS928371",
                        epsExit: "Active",
                        avatarUrl: "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&q=80&w=128"
                    }
                ];
            }

            renderDashboard();
            showToast(`Approved admin ${admins[adminIndex].name || 'Administrator'}. Company Code ${admins[adminIndex].companyCode} activated.`, "success");
        }
    }

    async function suspendAdmin(id) {
        const adminIndex = admins.findIndex(a => a.id === id);
        if (adminIndex > -1) {
            admins[adminIndex].status = 'SUSPENDED';
            saveState();

            try {
                await fetch(`${API_BASE_URL}/super-admin/companies/${id}/status?status=SUSPENDED`, {
                    method: 'POST'
                });
            } catch (e) {
                console.log("Could not update status on backend:", e.message);
            }

            renderDashboard();
            showToast(`Suspended account for admin ${admins[adminIndex].name || 'Administrator'}.`, "warning");
        }
    }

    // Modal Operations for Features Toggling
    function openFeatureModal(id) {
        const admin = admins.find(a => a.id === id);
        if (!admin) return;

        activeEditAdminId = id;
        modalAdminName.textContent = admin.name;
        modalAdminEmail.textContent = admin.email;
        modalAdminCompany.textContent = `${admin.companyName} (Code: ${admin.companyCode})`;

        // Populate checkboxes
        for (const feature in featureCheckboxes) {
            featureCheckboxes[feature].checked = admin.allowedFeatures.includes(feature);
        }

        // Show Modal
        featureModal.classList.remove('hidden');
    }

    function closeFeatureModal() {
        featureModal.classList.add('hidden');
        activeEditAdminId = null;
    }

    async function saveFeatures() {
        if (!activeEditAdminId) return;

        const adminIndex = admins.findIndex(a => a.id === activeEditAdminId);
        if (adminIndex > -1) {
            const updatedFeatures = [];
            for (const feature in featureCheckboxes) {
                if (featureCheckboxes[feature].checked) {
                    updatedFeatures.push(feature);
                }
            }

            admins[adminIndex].allowedFeatures = updatedFeatures;
            saveState();

            try {
                await fetch(`${API_BASE_URL}/super-admin/companies/${activeEditAdminId}/features`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(updatedFeatures)
                });
            } catch (e) {
                console.log("Could not save features to backend:", e.message);
            }

            renderDashboard();
            closeFeatureModal();
            showToast(`Successfully updated feature permissions for ${admins[adminIndex].name || 'Administrator'}.`, "success");
        }
    }

    // Employee Profile Folder Modal
    async function openEmployeeProfileModal(emp, companyCode) {
        // Show modal immediately with basic info while loading
        const loadingText = 'Loading...';
        const na = 'Not available';

        const avatarSrc = emp.imageUrl || getAvatarUrl(emp.name || 'E');
        empModalAvatar.src = avatarSrc;
        empModalAvatar.onerror = () => { empModalAvatar.src = getAvatarUrl(emp.name || 'E'); };
        empModalName.textContent = emp.name || 'Unknown';
        empModalDesignation.textContent = loadingText;
        empModalDeptBadge.textContent = loadingText;
        empModalCodeBadge.textContent = companyCode;

        // Clear all fields to loading state
        [empModalGender, empModalMarital, empModalBlood, empModalHandicapped,
         empModalCurrentAddress, empModalPermanentAddress, empModalFather, empModalMother,
         empModalEmergencyName, empModalEmergencyRelation, empModalEmergencyPhone,
         empModalBankName, empModalBankHolder, empModalBankNumber, empModalBankIfsc, empModalBankUpi,
         empModalUan, empModalPan, empModalPf, empModalPfDate, empModalEsi, empModalEsiDate,
         empModalEps, empModalEpsExit].forEach(el => { if (el) el.textContent = loadingText; });

        employeeModal.classList.remove('hidden');

        // Fetch full profile from backend
        let profile = null;
        try {
            const response = await fetch(`${API_BASE_URL}/users/${emp.id}`);
            if (response.ok) {
                profile = await response.json();
            } else {
                throw new Error('HTTP ' + response.status);
            }
        } catch (e) {
            console.log('Could not fetch full profile from backend:', e.message);
        }

        // Use backend data if available, otherwise use what we have from list + mark rest N/A
        const user = profile || emp;

        const positionLabel = user.position
            ? String(user.position).replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase())
            : (user.designation || na);
        const deptLabel = user.department
            ? String(user.department).replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, c => c.toUpperCase())
            : (user.dept || na);
        const roleLabel = user.role
            ? String(user.role).replace('ROLE_', '').replace(/_/g, ' ')
            : na;

        // Update avatar if backend has imageUrl
        if (user.imageUrl) {
            empModalAvatar.src = user.imageUrl;
        }
        empModalName.textContent = user.name || 'Unknown';
        empModalDesignation.textContent = `${positionLabel} · ${roleLabel}`;
        empModalDeptBadge.textContent = deptLabel;
        empModalCodeBadge.textContent = user.companyCode || companyCode;        // Personal info
        const set = (el, val) => { if (el) el.textContent = val || na; };
        set(empModalGender, user.gender);
        set(empModalMarital, user.maritalStatus);
        set(empModalBlood, user.bloodGroup);
        set(empModalHandicapped, user.physicallyChallenged);
        set(empModalCurrentAddress, user.currentAddress);
        set(empModalPermanentAddress, user.permanentAddress);

        // Family / emergency
        set(empModalFather, user.fathersName);
        set(empModalMother, user.mothersName);
        set(empModalEmergencyName, user.emergencyName);
        set(empModalEmergencyRelation, user.emergencyRelation);
        set(empModalEmergencyPhone, user.emergencyNumber || user.phone);

        // Bank details
        set(empModalBankName, user.bankName);
        set(empModalBankHolder, user.accountHolder);
        set(empModalBankNumber, user.accountNumber);
        set(empModalBankIfsc, user.ifscCode);
        set(empModalBankUpi, user.upiId);

        // Statutory
        set(empModalUan, user.uan);
        set(empModalPan, user.pan);
        set(empModalPf, user.pfNumber);
        set(empModalPfDate, user.pfJoining);
        set(empModalEsi, user.esiNumber);
        set(empModalEsiDate, user.esiJoining);
        set(empModalEps, user.epsNumber);
        set(empModalEpsExit, user.epsExit);

        // Show extra info we DO have — email and phone in the subtitle
        const subtitleEl = document.getElementById('emp-modal-subtitle');
        if (subtitleEl) {
            subtitleEl.innerHTML = `
                <span style="display:inline-flex;align-items:center;gap:4px;margin-right:12px;">
                    <span class="material-symbols-outlined" style="font-size:14px;">mail</span>
                    ${user.email || na}
                </span>
                ${user.phone ? `<span style="display:inline-flex;align-items:center;gap:4px;">
                    <span class="material-symbols-outlined" style="font-size:14px;">phone</span>
                    ${user.phone}
                </span>` : ''}
            `;
        }
    }

    function closeEmployeeModal() {
        employeeModal.classList.add('hidden');
    }


    // Generate Mock Registration
    function generateNewRegistration() {
        const names = ["Aakash Sharma", "Meera Nair", "Amit Patel", "Shalini Gupta", "Divya Teja"];
        const companies = ["Nexa Tech solutions", "Green Leaf HR", "Finvest Capital", "Cognitive Corp", "Summit logistics"];
        const domains = ["nexatech.com", "greenleaf.in", "finvest.co", "cognitive.org", "summit.net"];
        
        const randomIndex = Math.floor(Math.random() * names.length);
        const name = names[randomIndex];
        const companyName = companies[randomIndex];
        const email = `${name.toLowerCase().replace(' ', '.')}@${domains[randomIndex]}`;
        const codeLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        const companyCode = codeLetters.charAt(Math.floor(Math.random() * 26)) + 
                            codeLetters.charAt(Math.floor(Math.random() * 26)) + 
                            codeLetters.charAt(Math.floor(Math.random() * 26)) + 
                            Math.floor(10 + Math.random() * 90);

        // Requested random features
        const allFeatures = ["ATTENDANCE", "LEAVES", "TASKS", "MEETINGS", "CHAT"];
        const allowedFeatures = allFeatures.filter(() => Math.random() > 0.4);
        if (allowedFeatures.length === 0) allowedFeatures.push("ATTENDANCE"); // At least one feature

        const newAdmin = {
            id: `adm_${Date.now()}`,
            name: name,
            email: email,
            companyName: companyName,
            companyCode: companyCode,
            status: "PENDING",
            allowedFeatures: allowedFeatures,
            createdAt: new Date().toISOString().slice(0, 16).replace('T', ' ')
        };

        admins.unshift(newAdmin);
        saveState();
        
        if (activeView === 'dashboard') {
            renderDashboard();
        } else {
            renderDirectoryView();
        }

        showToast(`Received new registration request from ${name} (${companyName})`, "info");
    }

    // 7. Setting up Event Listeners
    function setupEventListeners() {
        // Sidebar tab switching
        sidebarMenuItems.forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                const view = e.currentTarget.getAttribute('data-section');
                switchView(view);
            });
        });

        // Search Input in dashboard
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                searchQuery = e.target.value;
                renderDashboard();
            });
        }

        // Feature filter select in dashboard
        if (featureFilter) {
            featureFilter.addEventListener('change', (e) => {
                selectedFeatureFilter = e.target.value;
                renderDashboard();
            });
        }

        // Tabs Toggle in dashboard
        tabLinks.forEach(tab => {
            tab.addEventListener('click', (e) => {
                tabLinks.forEach(t => t.classList.remove('active'));
                e.target.classList.add('active');
                
                currentTab = e.target.getAttribute('data-tab');
                renderDashboard();
            });
        });

        // Close Customization Modal Handlers
        btnCloseModal.addEventListener('click', closeFeatureModal);
        btnCancelModal.addEventListener('click', closeFeatureModal);
        btnSaveFeatures.addEventListener('click', saveFeatures);
        
        featureModal.addEventListener('click', (e) => {
            if (e.target === featureModal) {
                closeFeatureModal();
            }
        });

        // Close Employee Profile Modal Handlers
        btnCloseEmpModal.onclick = closeEmployeeModal;
        btnCloseEmpModalFooter.onclick = closeEmployeeModal;
        employeeModal.addEventListener('click', (e) => {
            if (e.target === employeeModal) {
                closeEmployeeModal();
            }
        });

        // Add Mock registration request
        btnAddMockAdmin.addEventListener('click', generateNewRegistration);

        // Attendance Monitor Load Button
        const btnLoadAttendance = document.getElementById('btn-load-attendance');
        if (btnLoadAttendance) {
            btnLoadAttendance.addEventListener('click', loadCompanyAttendance);
        }

        const btnModeDaily = document.getElementById('btn-mode-daily');
        const btnModeCalendar = document.getElementById('btn-mode-calendar');
        if (btnModeDaily && btnModeCalendar) {
            btnModeDaily.addEventListener('click', () => {
                attendanceViewMode = 'daily';
                btnModeDaily.style.background = '#4f46e5';
                btnModeDaily.style.color = 'white';
                btnModeCalendar.style.background = 'transparent';
                btnModeCalendar.style.color = '#94a3b8';
                
                document.getElementById('attn-user-group').style.display = 'none';
                document.getElementById('attn-date-group').style.display = 'block';
                document.getElementById('attn-month-group').style.display = 'none';
                
                document.getElementById('attn-daily-view-container').style.display = 'block';
                document.getElementById('attn-calendar-view-container').style.display = 'none';
                
                document.getElementById('attendance-table-body').innerHTML = '';
                document.getElementById('attn-empty-state').style.display = 'flex';
            });

            btnModeCalendar.addEventListener('click', () => {
                attendanceViewMode = 'calendar';
                btnModeCalendar.style.background = '#4f46e5';
                btnModeCalendar.style.color = 'white';
                btnModeDaily.style.background = 'transparent';
                btnModeDaily.style.color = '#94a3b8';
                
                document.getElementById('attn-user-group').style.display = 'block';
                document.getElementById('attn-date-group').style.display = 'none';
                document.getElementById('attn-month-group').style.display = 'block';
                
                document.getElementById('attn-daily-view-container').style.display = 'none';
                document.getElementById('attn-calendar-view-container').style.display = 'block';
                
                document.getElementById('calendar-days-grid').innerHTML = '';
                document.getElementById('attn-empty-state').style.display = 'flex';

                const companyCode = document.getElementById('attn-company-select').value;
                if (companyCode) {
                    populateAttendanceUsers(companyCode);
                }
            });
        }

        const attnCompanySelect = document.getElementById('attn-company-select');
        if (attnCompanySelect) {
            attnCompanySelect.addEventListener('change', (e) => {
                if (attendanceViewMode === 'calendar') {
                    populateAttendanceUsers(e.target.value);
                }
            });
        }

        // Location Trail Select changes
        const trailCompanySelect = document.getElementById('trail-company-select');
        if (trailCompanySelect) {
            trailCompanySelect.addEventListener('change', (e) => {
                loadTrailEmployees(e.target.value);
            });
        }

        const btnLoadTrail = document.getElementById('btn-load-trail');
        if (btnLoadTrail) {
            btnLoadTrail.addEventListener('click', () => {
                if (liveTrackingActive) stopLiveTracking();
                loadAndRenderTrail();
            });
        }

        const btnLiveTrack = document.getElementById('btn-live-track');
        if (btnLiveTrack) {
            btnLiveTrack.addEventListener('click', () => {
                if (liveTrackingActive) {
                    stopLiveTracking();
                } else {
                    startLiveTracking();
                }
            });
        }

        // Auto-stop live tracking when employee selection changes
        const trailEmpSelect = document.getElementById('trail-employee-select');
        if (trailEmpSelect) {
            trailEmpSelect.addEventListener('change', () => {
                if (liveTrackingActive) stopLiveTracking();
            });
        }
    }


    function populateTrailCompanies() {
        const select = document.getElementById('trail-company-select');
        if (!select) return;
        select.innerHTML = '<option value="">-- Choose Company --</option>';
        
        const companies = admins.filter(a => a.status === 'APPROVED');
        companies.forEach(company => {
            const opt = document.createElement('option');
            opt.value = company.companyCode;
            opt.textContent = `${company.companyName} (${company.companyCode})`;
            select.appendChild(opt);
        });
    }

    async function loadTrailEmployees(companyCode) {
        const empSelect = document.getElementById('trail-employee-select');
        if (!empSelect) return;
        empSelect.innerHTML = '<option value="">-- Choose Employee --</option>';
        empSelect.disabled = true;

        if (!companyCode) return;

        let employees = [];
        try {
            const response = await fetch(`${API_BASE_URL}/companies/code/${companyCode}/employees`);
            if (response.ok) {
                employees = await response.json();
            } else {
                throw new Error("Backend response error");
            }
        } catch (e) {
            console.log("Falling back to mock employees due to:", e.message);
            employees = mockEmployees[companyCode] || [];
        }

        if (employees.length > 0) {
            employees.forEach(emp => {
                const opt = document.createElement('option');
                opt.value = emp.id;
                opt.textContent = emp.name;
                empSelect.appendChild(opt);
            });
            empSelect.disabled = false;
        } else {
            showToast("No active employee directory found for this company.", "warning");
        }
    }

    async function loadAndRenderTrail() {
        const companyCode = document.getElementById('trail-company-select').value;
        const employeeId = document.getElementById('trail-employee-select').value;
        const date = document.getElementById('trail-date-select').value;

        if (!companyCode || !employeeId || !date) {
            showToast("Please select company, employee, and date first.", "warning");
            return;
        }

        let trailPoints = [];
        try {
            const response = await fetch(`${API_BASE_URL}/attendances/trail/employee/${employeeId}?date=${date}`);
            if (response.ok) {
                trailPoints = await response.json();
            } else {
                throw new Error("Backend query failed");
            }
        } catch (e) {
            console.log("Generating mock trail points due to:", e.message);
            trailPoints = generateMockTrailPoints();
        }

        renderTrailOnMap(trailPoints);
    }

    function generateMockTrailPoints() {
        const now = new Date();
        const points = [];
        const startLat = 12.9716;
        const startLng = 77.5946;
        
        for (let i = 0; i < 6; i++) {
            const time = new Date(now.getTime() - (6 - i) * 1800 * 1000);
            points.push({
                latitude: startLat + (i * 0.0015) - (Math.random() * 0.0005),
                longitude: startLng + (i * 0.0012) + (Math.random() * 0.0005),
                recordedAt: time.toISOString()
            });
        }
        return points;
    }

    // ========== LIVE TRACKING FUNCTIONS ==========

    async function startLiveTracking() {
        const employeeId = document.getElementById('trail-employee-select').value;
        const companyCode = document.getElementById('trail-company-select').value;

        if (!companyCode || !employeeId) {
            showToast('Please select a company and employee first.', 'warning');
            return;
        }

        // Fetch employee details for the sidebar panel
        let empData = { name: 'Employee', imageUrl: '' };
        try {
            const r = await fetch(`${API_BASE_URL}/users/${employeeId}`);
            if (r.ok) empData = await r.json();
        } catch(e) { console.log('Could not prefetch employee data', e); }
        liveEmployeeData = empData;

        // Load today's historical trail first so the path context is visible
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('trail-date-select').value = today;
        let historicalPoints = [];
        try {
            const r = await fetch(`${API_BASE_URL}/attendances/trail/employee/${employeeId}?date=${today}`);
            if (r.ok) historicalPoints = await r.json();
        } catch(e) { console.log('Could not load historical trail', e); }

        // Render existing trail before going live
        if (historicalPoints.length > 0) {
            renderTrailOnMap(historicalPoints);
            livePolylinePoints = historicalPoints.map(p => [p.latitude, p.longitude]);
        } else {
            // Initialise the map even with no historical data
            const placeholder = document.getElementById('map-placeholder');
            if (placeholder) placeholder.style.display = 'none';
            if (!trailMap) {
                trailMap = L.map('trail-map').setView([28.6139, 77.2090], 13);
                L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
                    attribution: '&copy; OpenStreetMap &copy; CARTO',
                    subdomains: 'abcd', maxZoom: 20
                }).addTo(trailMap);
            }
            livePolylinePoints = [];
        }

        liveEmployeeId = employeeId;
        livePointCount = historicalPoints.length;
        liveTrackingActive = true;

        // Update UI
        const btnLive = document.getElementById('btn-live-track');
        btnLive.classList.add('active');
        btnLive.innerHTML = '<span class="material-symbols-outlined">sensors_off</span> Stop Live Tracking';

        document.getElementById('live-status-badge').classList.remove('hidden');

        const panel = document.getElementById('live-employee-panel');
        panel.classList.remove('hidden');
        document.getElementById('live-emp-name').textContent = empData.name || 'Employee';
        document.getElementById('live-emp-avatar').src = empData.imageUrl || getAvatarUrl(empData.name || 'E');
        document.getElementById('live-points-count').textContent = livePointCount;
        document.getElementById('live-last-update').textContent = historicalPoints.length > 0
            ? new Date(historicalPoints[historicalPoints.length - 1].recordedAt).toLocaleTimeString()
            : 'Waiting...';

        // Connect WebSocket via SockJS + STOMP
        const socket = new SockJS(`${API_BASE_URL}/chat-websocket`);
        stompClient = Stomp.over(socket);
        stompClient.debug = null; // Silence STOMP debug logs

        stompClient.connect({}, () => {
            liveSubscription = stompClient.subscribe(`/topic/location/${employeeId}`, (message) => {
                const point = JSON.parse(message.body);
                appendLivePoint(point);
            });
            showToast('🔴 Live tracking started for ' + (empData.name || 'employee') + '.', 'success');
        }, (error) => {
            console.error('STOMP connection error:', error);
            showToast('Could not connect to live tracking server.', 'danger');
            stopLiveTracking();
        });
    }

    function stopLiveTracking() {
        liveTrackingActive = false;

        // Unsubscribe and disconnect STOMP
        try {
            if (liveSubscription) liveSubscription.unsubscribe();
            if (stompClient && stompClient.connected) stompClient.disconnect();
        } catch(e) { console.log('STOMP disconnect error', e); }

        stompClient = null;
        liveSubscription = null;
        liveEmployeeId = null;
        liveEmployeeData = null;
        livePolylinePoints = [];
        livePointCount = 0;

        // Remove live marker from map
        if (liveMarker) {
            trailMap && trailMap.removeLayer(liveMarker);
            liveMarker = null;
        }

        // Reset UI
        const btnLive = document.getElementById('btn-live-track');
        if (btnLive) {
            btnLive.classList.remove('active');
            btnLive.innerHTML = '<span class="material-symbols-outlined">sensors</span> Start Live Tracking';
        }
        document.getElementById('live-status-badge').classList.add('hidden');
        document.getElementById('live-employee-panel').classList.add('hidden');

        showToast('Live tracking stopped.', 'info');
    }

    function appendLivePoint(point) {
        if (!liveTrackingActive || !trailMap) return;

        const lat = point.latitude;
        const lng = point.longitude;
        const latlng = [lat, lng];

        livePolylinePoints.push(latlng);
        livePointCount++;

        // Extend or create the polyline
        if (trailPolyline) {
            trailPolyline.setLatLngs(livePolylinePoints);
        } else {
            trailPolyline = L.polyline(livePolylinePoints, {
                color: '#6366f1', weight: 5, opacity: 0.85
            }).addTo(trailMap);
        }

        // Move / create the pulsing live marker
        const liveIcon = L.divIcon({
            html: '<div class="live-pulse-marker"></div>',
            className: '',
            iconSize: [20, 20],
            iconAnchor: [10, 10]
        });

        if (liveMarker) {
            liveMarker.setLatLng(latlng);
        } else {
            liveMarker = L.marker(latlng, { icon: liveIcon, zIndexOffset: 1000 })
                .bindPopup('<strong>Live Position</strong>')
                .addTo(trailMap);
        }

        // Pan map smoothly to new position
        trailMap.panTo(latlng, { animate: true, duration: 0.8 });

        // Update sidebar stats
        document.getElementById('live-points-count').textContent = livePointCount;
        document.getElementById('live-last-update').textContent = new Date(point.recordedAt).toLocaleTimeString();
        document.getElementById('live-current-coords').textContent =
            `${lat.toFixed(5)}, ${lng.toFixed(5)}`;

        // Update session summary total points
        const summaryPts = document.getElementById('trail-summary-points');
        if (summaryPts) summaryPts.textContent = livePointCount;
        document.getElementById('trail-info-panel').classList.remove('hidden');
    }

    function renderTrailOnMap(points) {

        const placeholder = document.getElementById('map-placeholder');
        if (placeholder) placeholder.style.display = 'none';

        if (points.length === 0) {
            showToast("No location trail logs found for this date.", "warning");
            if (placeholder) placeholder.style.display = 'flex';
            document.getElementById('trail-info-panel').classList.add('hidden');
            return;
        }

        const startPt = points[0];
        const endPt = points[points.length - 1];

        // Initialize Map if not done
        if (!trailMap) {
            trailMap = L.map('trail-map').setView([startPt.latitude, startPt.longitude], 14);
            L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
                subdomains: 'abcd',
                maxZoom: 20
            }).addTo(trailMap);
        }

        // Clear existing markers and lines
        if (trailPolyline) {
            trailMap.removeLayer(trailPolyline);
        }
        trailMarkers.forEach(m => trailMap.removeLayer(m));
        trailMarkers = [];

        // Parse latlngs
        const latlngs = points.map(pt => [pt.latitude, pt.longitude]);

        // Draw Polyline
        trailPolyline = L.polyline(latlngs, {color: '#6366f1', weight: 5, opacity: 0.85}).addTo(trailMap);

        // Check-In green marker
        const greenIcon = L.divIcon({
            html: '<span class="material-symbols-outlined" style="color: #10b981; font-size: 32px; font-weight: bold;">location_on</span>',
            className: 'custom-div-icon',
            iconSize: [32, 32],
            iconAnchor: [16, 32]
        });
        const startMarker = L.marker([startPt.latitude, startPt.longitude], {icon: greenIcon})
            .bindPopup(`<strong>Check-In</strong><br>Time: ${new Date(startPt.recordedAt).toLocaleTimeString()}`)
            .addTo(trailMap);
        trailMarkers.push(startMarker);

        // Check-Out red marker
        const redIcon = L.divIcon({
            html: '<span class="material-symbols-outlined" style="color: #ef4444; font-size: 32px; font-weight: bold;">location_on</span>',
            className: 'custom-div-icon',
            iconSize: [32, 32],
            iconAnchor: [16, 32]
        });
        const endMarker = L.marker([endPt.latitude, endPt.longitude], {icon: redIcon})
            .bindPopup(`<strong>Check-Out</strong><br>Time: ${new Date(endPt.recordedAt).toLocaleTimeString()}`)
            .addTo(trailMap);
        trailMarkers.push(endMarker);

        // Intermediate circle dots
        for (let i = 1; i < points.length - 1; i++) {
            const pt = points[i];
            const circle = L.circleMarker([pt.latitude, pt.longitude], {
                radius: 6,
                color: '#6366f1',
                fillColor: '#1e1b4b',
                fillOpacity: 1,
                weight: 2
            }).bindPopup(`Logged at: ${new Date(pt.recordedAt).toLocaleTimeString()}`).addTo(trailMap);
            trailMarkers.push(circle);
        }

        // Auto zoom and center trail boundaries
        trailMap.fitBounds(trailPolyline.getBounds(), {padding: [50, 50]});

        // Display Summary Card details
        document.getElementById('trail-info-panel').classList.remove('hidden');
        document.getElementById('trail-summary-checkin').textContent = new Date(startPt.recordedAt).toLocaleTimeString();
        document.getElementById('trail-summary-checkout').textContent = points.length > 1 ? new Date(endPt.recordedAt).toLocaleTimeString() : "Still active";
        document.getElementById('trail-summary-points').textContent = points.length;

        showToast(`Loaded location trail with ${points.length} points.`, "success");
    }

    // 8. Helper Functions
    function getFeatureIcon(feature) {
        switch (feature) {
            case 'ATTENDANCE': return 'where_to_vote';
            case 'LEAVES': return 'calendar_today';
            case 'TASKS': return 'assignment';
            case 'MEETINGS': return 'groups';
            case 'CHAT': return 'chat';
            default: return 'help';
        }
    }

    function getAvatarUrl(name) {
        const safeName = name || "System Admin";
        return `https://ui-avatars.com/api/?name=${encodeURIComponent(safeName)}&background=6366f1&color=fff&bold=true&size=128`;
    }

    // Toast System
    function showToast(message, type = 'info') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        
        let icon = 'info';
        if (type === 'success') icon = 'check_circle';
        if (type === 'danger') icon = 'error';
        if (type === 'warning') icon = 'warning';

        toast.innerHTML = `
            <span class="material-symbols-outlined toast-icon">${icon}</span>
            <div class="toast-message">${message}</div>
            <button class="toast-close">
                <span class="material-symbols-outlined">close</span>
            </button>
        `;

        container.appendChild(toast);

        toast.querySelector('.toast-close').addEventListener('click', () => {
            toast.style.animation = 'none';
            toast.offsetHeight;
            toast.style.animation = 'toast-slide-out 0.3s forwards';
            setTimeout(() => toast.remove(), 300);
        });

    }

    function populateAttendanceCompanies() {
        const select = document.getElementById('attn-company-select');
        if (!select) return;
        select.innerHTML = '<option value="">-- Choose Company --</option>';
        
        const companies = admins.filter(a => a.status === 'APPROVED');
        companies.forEach(company => {
            const opt = document.createElement('option');
            opt.value = company.companyCode;
            opt.textContent = `${company.companyName} (${company.companyCode})`;
            select.appendChild(opt);
        });

        const dateInput = document.getElementById('attn-date-select');
        if (dateInput && !dateInput.value) {
            dateInput.value = new Date().toISOString().split('T')[0];
        }

        const monthInput = document.getElementById('attn-month-select');
        if (monthInput && !monthInput.value) {
            const today = new Date();
            monthInput.value = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}`;
        }
    }

    async function loadCompanyAttendance() {
        const companyCode = document.getElementById('attn-company-select').value;
        const emptyState = document.getElementById('attn-empty-state');

        if (!companyCode) {
            showToast("Please choose a company first.", "warning");
            return;
        }

        if (attendanceViewMode === 'daily') {
            const date = document.getElementById('attn-date-select').value;
            const tbody = document.getElementById('attendance-table-body');
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 24px; color: #94a3b8;">Loading attendance data...</td></tr>';
            emptyState.style.display = 'none';

            try {
                const response = await fetch(`${API_BASE_URL}/super-admin/companies/${companyCode}/attendances?date=${date}`);
                if (response.ok) {
                    const logs = await response.json();
                    tbody.innerHTML = '';
                    
                    if (logs.length === 0) {
                        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 24px; color: #94a3b8;">No registered users found in this company.</td></tr>';
                        return;
                    }

                    logs.forEach(log => {
                        const tr = document.createElement('tr');
                        tr.style.borderBottom = '1px solid rgba(255, 255, 255, 0.05)';
                        
                        const userInfoHTML = `
                            <div class="admin-info-cell" style="display: flex; align-items: center; gap: 10px; padding: 12px 16px;">
                                <img class="admin-avatar" src="${getAvatarUrl(log.name)}" alt="${log.name}" style="width: 32px; height: 32px; border-radius: 50%; object-fit: cover;">
                                <div class="admin-name-wrapper">
                                    <h4 style="margin: 0; color: #f8fafc; font-size: 14px; font-weight: 500;">${log.name}</h4>
                                    <span style="font-size: 12px; color: #64748b;">${log.email}</span>
                                </div>
                            </div>
                        `;

                        let roleDisplay = "Employee";
                        let roleBg = "rgba(99, 102, 241, 0.15)";
                        let roleColor = "#818cf8";
                        if (log.role === "ROLE_ADMIN") {
                            roleDisplay = "Admin";
                            roleBg = "rgba(245, 158, 11, 0.15)";
                            roleColor = "#fbbf24";
                        } else if (log.role === "ROLE_HR") {
                            roleDisplay = "HR";
                            roleBg = "rgba(16, 185, 129, 0.15)";
                            roleColor = "#34d399";
                        }

                        const roleHTML = `<span style="display: inline-block; padding: 4px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; background: ${roleBg}; color: ${roleColor};">${roleDisplay}</span>`;

                        let statusBg = "rgba(71, 85, 105, 0.2)";
                        let statusColor = "#94a3b8";
                        if (log.status === "Checked Out") {
                            statusBg = "rgba(239, 68, 68, 0.15)";
                            statusColor = "#f87171";
                        } else if (log.status === "Checked In") {
                            statusBg = "rgba(16, 185, 129, 0.15)";
                            statusColor = "#34d399";
                        }

                        const statusHTML = `<span style="display: inline-block; padding: 4px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; background: ${statusBg}; color: ${statusColor};">${log.status}</span>`;

                        const formatTime = (timeStr) => {
                            if (!timeStr) return "-";
                            return new Date(timeStr).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                        };
                        const checkInTime = formatTime(log.checkIn);
                        const checkOutTime = formatTime(log.checkOut);

                        const coords = log.latitude && log.longitude ? `${parseFloat(log.latitude).toFixed(4)}, ${parseFloat(log.longitude).toFixed(4)}` : "-";

                        tr.innerHTML = `
                            <td style="padding: 0;">${userInfoHTML}</td>
                            <td style="padding: 12px 16px;">${roleHTML}</td>
                            <td style="padding: 12px 16px;">${statusHTML}</td>
                            <td style="padding: 12px 16px; color: #e2e8f0; font-size: 14px;">${checkInTime}</td>
                            <td style="padding: 12px 16px; color: #e2e8f0; font-size: 14px;">${checkOutTime}</td>
                            <td style="padding: 12px 16px; color: #94a3b8; font-size: 13px; font-family: monospace;">${coords}</td>
                        `;
                        tbody.appendChild(tr);
                    });
                } else {
                    throw new Error("HTTP error: " + response.status);
                }
            } catch (e) {
                console.error("Failed to load attendance logs", e);
                tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 24px; color: #ef4444;">Failed to retrieve records from backend. Make sure the server is running.</td></tr>';
                showToast("Could not load attendance logs.", "danger");
            }
        } else {
            const userId = document.getElementById('attn-user-select').value;
            const month = document.getElementById('attn-month-select').value;
            const grid = document.getElementById('calendar-days-grid');

            if (!userId) {
                showToast("Please select an employee or admin first.", "warning");
                return;
            }
            if (!month) {
                showToast("Please select a month first.", "warning");
                return;
            }

            grid.innerHTML = '<div style="grid-column: span 7; text-align: center; padding: 40px; color: #94a3b8;">Loading calendar view...</div>';
            emptyState.style.display = 'none';

            try {
                const response = await fetch(`${API_BASE_URL}/super-admin/users/${userId}/monthly-attendance?month=${month}`);
                if (response.ok) {
                    const logs = await response.json();
                    renderMonthlyCalendar(month, logs);
                } else {
                    throw new Error("HTTP error: " + response.status);
                }
            } catch (e) {
                console.error("Failed to load monthly attendance", e);
                grid.innerHTML = '<div style="grid-column: span 7; text-align: center; padding: 40px; color: #ef4444;">Failed to retrieve monthly logs from backend.</div>';
                showToast("Could not load monthly attendance.", "danger");
            }
        }
    }

    async function populateAttendanceUsers(companyCode) {
        const userSelect = document.getElementById('attn-user-select');
        if (!userSelect) return;
        userSelect.innerHTML = '<option value="">-- Select Employee/Admin --</option>';

        if (!companyCode) return;

        const adminUser = admins.find(a => a.companyCode === companyCode);
        if (adminUser) {
            const opt = document.createElement('option');
            // adminId is the HR user's own DB ID; .id is the company's DB ID — must use adminId here
            opt.value = adminUser.adminId || adminUser.id;
            opt.textContent = `${adminUser.adminName || adminUser.name} (Admin)`;
            userSelect.appendChild(opt);
        }

        try {
            const response = await fetch(`${API_BASE_URL}/companies/code/${companyCode}/employees`);
            if (response.ok) {
                const employees = await response.json();
                employees.forEach(emp => {
                    if (adminUser && adminUser.id === emp.id) return;
                    
                    const opt = document.createElement('option');
                    opt.value = emp.id;
                    opt.textContent = `${emp.name} (Employee)`;
                    userSelect.appendChild(opt);
                });
            }
        } catch (e) {
            console.error("Failed to load company employees", e);
        }
    }

    function renderMonthlyCalendar(monthStr, attendanceLogs) {
        const grid = document.getElementById('calendar-days-grid');
        if (!grid) return;
        grid.innerHTML = '';

        const [year, month] = monthStr.split('-').map(Number);
        
        const firstDayDate = new Date(year, month - 1, 1);
        const startDayOfWeek = firstDayDate.getDay();
        const daysInMonth = new Date(year, month, 0).getDate();

        let presentCount = 0;
        let missingCount = 0;
        let absentCount = 0;

        const logsMap = {};
        attendanceLogs.forEach(log => {
            logsMap[log.date] = log;
        });

        for (let i = 0; i < startDayOfWeek; i++) {
            const cell = document.createElement('div');
            cell.style.height = '60px';
            grid.appendChild(cell);
        }

        const todayStr = new Date().toISOString().split('T')[0];
        const todayDate = new Date();
        todayDate.setHours(0, 0, 0, 0);

        for (let day = 1; day <= daysInMonth; day++) {
            const currentDate = new Date(year, month - 1, day);
            const dateStr = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
            const log = logsMap[dateStr];

            const cell = document.createElement('div');
            cell.style.height = '60px';
            cell.style.borderRadius = '8px';
            cell.style.display = 'flex';
            cell.style.flexDirection = 'column';
            cell.style.alignItems = 'center';
            cell.style.justifyContent = 'center';
            cell.style.fontSize = '14px';
            cell.style.position = 'relative';
            cell.style.transition = 'all 0.2s';
            cell.style.border = '1px solid rgba(255,255,255,0.03)';

            const dayLabel = document.createElement('span');
            dayLabel.textContent = day;
            dayLabel.style.fontWeight = '500';
            dayLabel.style.color = '#e2e8f0';
            cell.appendChild(dayLabel);

            const dayOfWeek = currentDate.getDay();
            const isWeekend = (dayOfWeek === 0 || dayOfWeek === 6);
            const isFuture = currentDate > todayDate;

            let bgColor = 'rgba(255, 255, 255, 0.02)';
            let borderColor = 'transparent';
            let indicatorColor = '';

            if (log) {
                if (log.status === 'Present') {
                    bgColor = 'rgba(76, 175, 80, 0.1)';
                    borderColor = 'rgba(76, 175, 80, 0.3)';
                    indicatorColor = '#4CAF50';
                    presentCount++;
                } else if (log.status === 'Missing Out') {
                    bgColor = 'rgba(245, 158, 11, 0.1)';
                    borderColor = 'rgba(245, 158, 11, 0.3)';
                    indicatorColor = '#f59e0b';
                    missingCount++;
                }
            } else if (!isFuture && !isWeekend) {
                bgColor = 'rgba(239, 68, 68, 0.1)';
                borderColor = 'rgba(239, 68, 68, 0.3)';
                indicatorColor = '#ef4444';
                absentCount++;
            }

            if (dateStr === todayStr) {
                cell.style.boxShadow = '0 0 0 2px #6366f1';
                dayLabel.style.color = '#818cf8';
                dayLabel.style.fontWeight = '700';
            }

            cell.style.background = bgColor;
            if (borderColor !== 'transparent') {
                cell.style.borderColor = borderColor;
            }

            if (indicatorColor) {
                const dot = document.createElement('span');
                dot.style.width = '6px';
                dot.style.height = '6px';
                dot.style.borderRadius = '50%';
                dot.style.background = indicatorColor;
                dot.style.marginTop = '4px';
                cell.appendChild(dot);
            }

            if (log) {
                cell.title = `Check-In: ${log.checkIn ? new Date(log.checkIn).toLocaleTimeString() : '-'}, Check-Out: ${log.checkOut ? new Date(log.checkOut).toLocaleTimeString() : '-'}`;
                cell.style.cursor = 'help';
            } else if (isWeekend) {
                cell.title = 'Weekend';
            } else if (isFuture) {
                cell.title = 'Future Date';
            } else {
                cell.title = 'Absent';
            }

            grid.appendChild(cell);
        }

        document.getElementById('cal-stat-present').textContent = presentCount;
        document.getElementById('cal-stat-missing').textContent = missingCount;
        document.getElementById('cal-stat-absent').textContent = absentCount;
    }

    // Run Initialization
    init();
});

