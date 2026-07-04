/**
 * CREWHQ - SUPER ADMIN PORTAL STATE & CONTROLLER LOGIC
 */

document.addEventListener('DOMContentLoaded', () => {
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

    // 1. Initialize Application State
    function init() {
        // Set date
        const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        currentDateEl.textContent = new Date().toLocaleDateString('en-US', options);

        // Load data from LocalStorage
        const savedAdmins = localStorage.getItem('crewHQ_admins');
        if (savedAdmins) {
            admins = JSON.parse(savedAdmins);
        } else {
            admins = initialAdmins;
            saveState();
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

    function loadCompanyDetails(company) {
        dirSelectPrompt.classList.add('hidden');
        dirDetailsContent.classList.remove('hidden');

        // Admin info
        dirAdminAvatar.src = getAvatarUrl(company.name);
        dirAdminName.textContent = company.name;
        dirAdminEmail.textContent = company.email;
        dirAdminCompany.textContent = company.companyName;
        dirAdminCode.textContent = company.companyCode;
        
        dirAdminStatus.textContent = company.status;
        dirAdminStatus.className = `badge-status ${company.status.toLowerCase()}`;

        // Admin features tags
        const features = ["ATTENDANCE", "LEAVES", "TASKS", "MEETINGS", "CHAT"];
        dirAdminFeatures.innerHTML = features.map(f => {
            const active = company.allowedFeatures.includes(f);
            return `<span class="feature-tag ${active ? 'active' : 'inactive'}">
                <span class="material-symbols-outlined" style="font-size:12px;">${getFeatureIcon(f)}</span>
                ${f.charAt(0) + f.slice(1).toLowerCase()}
            </span>`;
        }).join('');

        // Employees list
        const employees = mockEmployees[company.companyCode] || [];
        dirEmployeeCount.textContent = `${employees.length} employee${employees.length === 1 ? '' : 's'}`;

        dirEmployeesListBody.innerHTML = '';
        if (employees.length === 0) {
            dirEmployeesListBody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding: 30px; color:var(--text-muted); font-size:13px;">No employee records registered in this company.</td></tr>`;
            return;
        }

        employees.forEach(emp => {
            const tr = document.createElement('tr');
            
            tr.innerHTML = `
                <td>
                    <div class="dir-emp-name-cell">
                        <img src="${emp.avatarUrl}" alt="${emp.name}" class="dir-emp-avatar">
                        <strong>${emp.name}</strong>
                    </div>
                </td>
                <td>${emp.email}</td>
                <td>
                    <div class="dir-emp-role-dept">
                        <span class="role">${emp.designation}</span>
                        <span class="dept">${emp.department}</span>
                    </div>
                </td>
                <td class="text-right">
                    <button class="btn btn-secondary btn-sm btn-view-emp-profile" data-id="${emp.id}">
                        <span class="material-symbols-outlined">badge</span> View Profile
                    </button>
                </td>
            `;

            tr.querySelector('.btn-view-emp-profile').onclick = () => {
                openEmployeeProfileModal(emp, company.companyCode);
            };

            dirEmployeesListBody.appendChild(tr);
        });
    }

    // 6. Action Handlers
    function approveAdmin(id) {
        const adminIndex = admins.findIndex(a => a.id === id);
        if (adminIndex > -1) {
            admins[adminIndex].status = 'APPROVED';
            saveState();
            
            // Generate mock employees if they don't exist yet for this code
            const code = admins[adminIndex].companyCode;
            if (!mockEmployees[code]) {
                mockEmployees[code] = [
                    {
                        id: `emp_${code}_01`,
                        name: `${admins[adminIndex].name.split(' ')[0]}'s Employee 1`,
                        email: `staff1@${admins[adminIndex].email.split('@')[1]}`,
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
            showToast(`Approved admin ${admins[adminIndex].name}. Company Code ${admins[adminIndex].companyCode} activated.`, "success");
        }
    }

    function suspendAdmin(id) {
        const adminIndex = admins.findIndex(a => a.id === id);
        if (adminIndex > -1) {
            admins[adminIndex].status = 'SUSPENDED';
            saveState();
            renderDashboard();
            showToast(`Suspended account for admin ${admins[adminIndex].name}.`, "warning");
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

    function saveFeatures() {
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
            renderDashboard();
            closeFeatureModal();
            showToast(`Successfully updated feature permissions for ${admins[adminIndex].name}.`, "success");
        }
    }

    // Employee Profile Folder Modal
    function openEmployeeProfileModal(emp, companyCode) {
        empModalAvatar.src = emp.avatarUrl;
        empModalName.textContent = emp.name;
        empModalDesignation.textContent = emp.designation;
        empModalDeptBadge.textContent = emp.department;
        empModalCodeBadge.textContent = companyCode;

        empModalGender.textContent = emp.gender;
        empModalMarital.textContent = emp.maritalStatus;
        empModalBlood.textContent = emp.bloodGroup;
        empModalHandicapped.textContent = emp.physicallyChallenged;
        empModalCurrentAddress.textContent = emp.currentAddress || "Not specified";
        empModalPermanentAddress.textContent = emp.permanentAddress || "Not specified";

        empModalFather.textContent = emp.fathersName || "Not specified";
        empModalMother.textContent = emp.mothersName || "Not specified";
        empModalEmergencyName.textContent = emp.emergencyName || "Not specified";
        empModalEmergencyRelation.textContent = emp.emergencyRelation || "Not specified";
        empModalEmergencyPhone.textContent = emp.emergencyNumber || "Not specified";

        empModalBankName.textContent = emp.bankName || "Not specified";
        empModalBankHolder.textContent = emp.accountHolder || "Not specified";
        empModalBankNumber.textContent = emp.accountNumber || "Not specified";
        empModalBankIfsc.textContent = emp.ifscCode || "Not specified";
        empModalBankUpi.textContent = emp.upiId || "Not specified";

        empModalUan.textContent = emp.uan || "Not specified";
        empModalPan.textContent = emp.pan || "Not specified";
        empModalPf.textContent = emp.pfNumber || "Not specified";
        empModalPfDate.textContent = emp.pfJoining || "Not specified";
        empModalEsi.textContent = emp.esiNumber || "Not specified";
        empModalEsiDate.textContent = emp.esiJoining || "Not specified";
        empModalEps.textContent = emp.epsNumber || "Not specified";
        empModalEpsExit.textContent = emp.epsExit || "Not specified";

        employeeModal.classList.remove('hidden');
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
        return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=6366f1&color=fff&bold=true&size=128`;
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

        setTimeout(() => {
            if (toast.parentNode) {
                toast.remove();
            }
        }, 4000);
    }

    // Run Initialization
    init();
});
