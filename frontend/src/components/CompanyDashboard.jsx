import { Link, Routes, Route, Navigate, useNavigate } from "react-router-dom";
import { Building2, LogOut, Briefcase, PlusCircle, Home } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import CompanyInternships from "./CompanyInternships";
import InternshipApplications from "./InternshipApplications";
import CreateInternship from "./CreateInternship";
//import CompanyCreateInternship from "./company/CompanyCreateInternship";
//import CompanyInternshipDetails from "./company/CompanyInternshipDetails";

export default function CompanyDashboard() {
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
            <Building2 className="h-7 w-7 text-indigo-600" />
            <span className="text-xl font-bold text-gray-900">Company</span>
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
          <TabLink label="Početna" to="/company/dashboard" icon={Home} />
          <TabLink label="Moje prakse" to="/company/dashboard/internships" icon={Briefcase} />
          <TabLink label="Kreiraj praksu" to="/company/dashboard/create" icon={PlusCircle} />
        </div>
      </div>

      {/* CONTENT */}
      <div className="max-w-7xl mx-auto px-6 py-8">
        <Routes>
          <Route path="/" element={<CompanyHome />} />
          <Route path="internships" element={<CompanyInternships />} />
          <Route path="internships/:id/applications" element={<InternshipApplications />} />
          <Route path="create" element={<CreateInternship />} />
          <Route path="*" element={<Navigate to="/company/dashboard" replace />} />
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

function CompanyHome() {
  return (
    <div className="text-center py-16">
      <h2 className="text-3xl font-bold text-gray-900 mb-4">Dobrodošli</h2>
      <p className="text-gray-600">
        Ovde možete upravljati oglasima za praksu i pregledati prijave studenata.
      </p>
    </div>
  );
}
