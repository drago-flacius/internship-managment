import { useState, useEffect } from "react";
import {
  Mail,
  Phone,
  BookOpen,
  Award,
  FileText,
  Save,
  Upload,
  Download,
  Trash2,
} from "lucide-react";
import { useAuth } from "../context/AuthContext";

const API_URL = "http://localhost:8080";

export default function StudentProfile() {
  const { token } = useAuth();

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [uploadingCV, setUploadingCV] = useState(false);

  const [cvInfo, setCvInfo] = useState(null);
  const [student, setStudent] = useState({
    id: null,
    firstName: "",
    lastName: "",
    email: "",
    indexNumber: "",
    phoneNumber: "",
    studyYear: "",
    bio: "",
    gpa: "",
    skills: "",
  });

  // Fetch profile + CV metadata
  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        // Fetch student profile
        const profileRes = await fetch(`${API_URL}/students/profile`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const profileResult = await profileRes.json();
        if (profileResult.success) {
          setStudent(profileResult.data);
          const studentId = profileResult.data.id;

          // Fetch CV metadata
          const cvMetaRes = await fetch(`${API_URL}/documents/${studentId}/cv/meta`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          if (cvMetaRes.ok) {
            const cvMeta = await cvMetaRes.json();
            if (cvMeta.success) setCvInfo(cvMeta.data);
          }
        }
      } catch (error) {
        console.error("Error fetching profile or CV:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [token]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setStudent((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    setSaving(true);
    try {
      const response = await fetch(`${API_URL}/students/profile`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(student),
      });
      const result = await response.json();
      if (result.success) alert("Profil uspešno ažuriran!");
      else alert("Greška pri čuvanju profila");
    } catch (error) {
      console.error("Error saving profile:", error);
      alert("Greška pri čuvanju profila");
    } finally {
      setSaving(false);
    }
  };

  const handleCVUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    if (file.type !== "application/pdf") {
      alert("Molimo učitajte PDF fajl");
      return;
    }

    if (file.size > 5 * 1024 * 1024) {
      alert("Fajl je prevelik. Maksimalna veličina je 5MB");
      return;
    }

    setUploadingCV(true);
    try {
      const formData = new FormData();
      formData.append("cv", file);

      const response = await fetch(`${API_URL}/documents/${student.id}/cv`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
        body: formData,
      });
      const result = await response.json();
      if (result.success) {
        setCvInfo(result.data);
        alert("CV uspešno učitan!");
      } else alert("Greška pri učitavanju CV-a");
    } catch (error) {
      console.error("Error uploading CV:", error);
      alert("Greška pri učitavanju CV-a");
    } finally {
      setUploadingCV(false);
      e.target.value = null;
    }
  };

  const handleCVDelete = async () => {
    if (!window.confirm("Da li ste sigurni da želite da obrišete CV?")) return;
    try {
      const response = await fetch(`${API_URL}/documents/${student.id}/cv`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      const result = await response.json();
      if (result.success) {
        setCvInfo(null);
        alert("CV uspešno obrisan!");
      } else alert("Greška pri brisanju CV-a");
    } catch (error) {
      console.error("Error deleting CV:", error);
      alert("Greška pri brisanju CV-a");
    }
  };

  const handleDownloadCV = async () => {
    if (!cvInfo) return;
    try {
      const response = await fetch(`${API_URL}/documents/${student.id}/cv`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = cvInfo.fileName;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error downloading CV:", error);
    }
  };

  if (loading) {
    return (
      <div className="max-w-4xl mx-auto p-6">
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
      </div>
    );
  }

  // === ORIGINAL UI STARTS ===
  return (
    <div className="max-w-4xl mx-auto p-6">
      <h1 className="text-3xl font-bold text-gray-900 mb-2">Moj Profil</h1>
      <p className="text-gray-600 mb-8">Ažurirajte svoje informacije</p>

      <div className="bg-white border border-gray-200 rounded-lg p-6">
        {/* Basic Info */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Ime</label>
            <input type="text" name="firstName" value={student.firstName} onChange={handleChange} className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Prezime</label>
            <input type="text" name="lastName" value={student.lastName} onChange={handleChange} className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
              <Mail className="w-4 h-4" /> Email
            </label>
            <input type="email" name="email" value={student.email} onChange={handleChange} className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
              <Phone className="w-4 h-4" /> Telefon
            </label>
            <input type="tel" name="phoneNumber" value={student.phoneNumber} onChange={handleChange} className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Broj indeksa</label>
            <input type="text" name="indexNumber" value={student.indexNumber} disabled className="w-full px-3 py-2 border rounded-lg bg-gray-50" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
              <BookOpen className="w-4 h-4" /> Godina studija
            </label>
            <select name="studyYear" value={student.studyYear} onChange={handleChange} className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500">
              <option value="">Izaberite godinu</option>
              <option value="1">Prva godina</option>
              <option value="2">Druga godina</option>
              <option value="3">Treća godina</option>
              <option value="4">Četvrta godina</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2 flex items-center gap-2">
              <Award className="w-4 h-4" /> Prosek (GPA)
            </label>
            <input type="number" step="0.01" name="gpa" value={student.gpa || ""} onChange={handleChange} placeholder="npr. 8.50" className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500" />
          </div>
        </div>

        {/* Bio */}
        <div className="mb-8">
          <label className="block text-sm font-medium text-gray-700 mb-2">O meni</label>
          <textarea name="bio" value={student.bio} onChange={handleChange} rows="4" className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500" />
        </div>

        {/* Skills */}
        <div className="mb-8">
          <label className="block text-sm font-medium text-gray-700 mb-2">Veštine</label>
          <textarea name="skills" value={student.skills} onChange={handleChange} rows="3" className="w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500" />
          <p className="text-sm text-gray-500 mt-1">Unesite veštine odvojene zarezima</p>
        </div>

        {/* CV */}
        <div className="mb-8">
          <h2 className="text-xl font-semibold text-gray-900 mb-4 flex items-center gap-2">
            <FileText className="w-5 h-5" /> CV (Curriculum Vitae)
          </h2>

          {cvInfo ? (
            <div className="border p-4 rounded-lg bg-gray-50">
              <div className="flex items-center justify-between mb-3">
                <div>
                  <p className="font-medium">{cvInfo.fileName}</p>
                  <p className="text-sm text-gray-500">
                    Učitano: {new Date(cvInfo.uploadedAt).toLocaleDateString("sr-RS")}
                  </p>
                </div>
                <div className="flex gap-2">
                  <button onClick={handleDownloadCV} className="flex items-center gap-2 text-blue-600 hover:bg-blue-50 px-3 py-2 rounded-lg text-sm">
                    <Download className="w-4 h-4" /> Preuzmi
                  </button>
                  <button onClick={handleCVDelete} className="flex items-center gap-2 text-red-600 hover:bg-red-50 px-3 py-2 rounded-lg text-sm">
                    <Trash2 className="w-4 h-4" /> Obriši
                  </button>
                </div>
              </div>
              <div className="pt-3 border-t border-gray-200">
                <label className="cursor-pointer text-blue-600 hover:text-blue-700 text-sm font-medium">
                  Zameni novi CV
                  <input type="file" accept=".pdf" onChange={handleCVUpload} disabled={uploadingCV} className="hidden" />
                </label>
              </div>
            </div>
          ) : (
            <div className="border-2 border-dashed rounded-lg p-6 text-center">
              {uploadingCV ? (
                <div className="flex flex-col items-center">
                  <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-3"></div>
                  <p className="text-gray-600">Učitavanje CV-a...</p>
                </div>
              ) : (
                <>
                  <Upload className="w-12 h-12 text-gray-400 mx-auto mb-3" />
                  <label className="cursor-pointer">
                    <span className="text-blue-600 hover:text-blue-700 font-medium">Učitaj CV</span>
                    <input type="file" accept=".pdf" onChange={handleCVUpload} className="hidden" />
                  </label>
                  <p className="text-sm text-gray-500 mt-2">PDF format, maks 5MB</p>
                </>
              )}
            </div>
          )}
        </div>

        {/* Save button */}
        <div className="flex justify-end">
          <button onClick={handleSave} disabled={saving} className="flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50">
            <Save className="w-5 h-5" /> {saving ? "Čuvanje..." : "Sačuvaj izmene"}
          </button>
        </div>
      </div>
    </div>
  );
}