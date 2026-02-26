import { Link, Routes, Route, Navigate, useNavigate } from "react-router-dom";
import { Users, LogOut, CheckCircle, FileText, Building2 } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import PendingInternships from "./PendingInternships";
import PendingApplications from "./PendingApplications";
import CompaniesOverview from "./CompaniesOverview";
import StudentsOverview from "./StudentsOverview";

export default function FacultyDashboard() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* NAVBAR */}
      <nav className="bg-white border-b shadow-sm">
        <div className="max-w-7xl mx-auto flex justify-between items-center h-16 px-6">
          <div className="flex items-center gap-3">
            <Users className="h-7 w-7 text-indigo-600" />
            <span className="text-xl font-bold text-gray-900">Referent</span>
          </div>
          <button
            onClick={handleLogout}
            className="flex items-center gap-2 px-3 py-1 rounded-md text-gray-600 hover:text-gray-900 hover:bg-gray-100 transition"
          >
            <LogOut className="h-5 w-5" />
            Odjavi se
          </button>
        </div>
      </nav>

      {/* TABS */}
      <div className="bg-white border-b">
        <div className="max-w-7xl mx-auto flex gap-4 px-6 py-3">
          <TabLink label="Početna" to="/faculty/dashboard" icon={Users} />
          <TabLink label="Prakse na čekanju" to="/faculty/dashboard/internships" icon={CheckCircle} />
          <TabLink label="Prijave na čekanju" to="/faculty/dashboard/applications" icon={FileText} />
          <TabLink label="Studenti" to="/faculty/dashboard/students" icon={Users} />
          <TabLink label="Kompanije" to="/faculty/dashboard/companies" icon={Building2} />
        </div>
      </div>

      {/* CONTENT */}
      <div className="max-w-7xl mx-auto px-6 py-8">
        <Routes>
          <Route path="/" element={<FacultyHome />} />
          <Route path="internships" element={<PendingInternships />} />
          <Route path="applications" element={<PendingApplications />} />
          <Route path="students" element={<StudentsOverview />} />
          <Route path="companies" element={<CompaniesOverview />} />
          <Route path="*" element={<Navigate to="/faculty/dashboard" replace />} />
        </Routes>
      </div>
    </div>
  );
}

function TabLink({ label, icon: Icon, to }) {
  return (
    <Link
      to={to}
      className="flex items-center gap-2 px-4 py-2 rounded-full font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-100 transition"
    >
      <Icon className="h-4 w-4" />
      {label}
    </Link>
  );
}

function FacultyHome() {
  return (
    <div className="text-center py-16">
      <h2 className="text-3xl font-bold text-gray-900 mb-4">Dobrodošli</h2>
      <p className="text-gray-600">
        Kao referent za stručne prakse pregledajte i odobrite prakse i prijave studenata.
      </p>
    </div>
  );
}


function Students() {
  return (
    <>
      <h2 className="text-2xl font-bold mb-4">Studenti</h2>
      <p className="text-gray-600">Pregled studenata i njihovih prijava.</p>
    </>
  );
}

