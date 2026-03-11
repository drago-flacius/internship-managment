import { useState, useEffect } from "react";
import { Upload, Download, Trash2 } from "lucide-react";
import { useAuth } from "../context/AuthContext";

const API_URL = "http://localhost:8080";

export default function InternshipDiary({ studentId }) {
    const { token } = useAuth();
    const [diaryInfo, setDiaryInfo] = useState(null);
    const [uploading, setUploading] = useState(false);

    useEffect(() => {
        const fetchDiary = async () => {
            try {
                const res = await fetch(`${API_URL}/documents/${studentId}/diary/metadata`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                const result = await res.json();
                if (result.success && result.data) {
                    setDiaryInfo(result.data);
                }
            } catch (error) {
                console.error("Error fetching diary metadata:", error);
            }
        };

        fetchDiary();
    }, [studentId, token]);

    // Upload handler
    const handleUpload = async (e) => {
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

        setUploading(true);
        try {
            const formData = new FormData();
            formData.append("file", file);

            const res = await fetch(`${API_URL}/documents/${studentId}/diary`, {
                method: "POST",
                headers: { Authorization: `Bearer ${token}` },
                body: formData,
            });

            const result = await res.json();
            if (res.ok) {
                alert("Dnevnik uspešno učitan!");
                setDiaryInfo({
                    filename: file.name,
                    uploadedAt: new Date().toISOString(),
                });
            } else {
                alert(result.message || "Greška pri učitavanju dnevnika");
            }
        } catch (error) {
            console.error(error);
            alert("Greška pri učitavanju dnevnika");
        } finally {
            setUploading(false);
            e.target.value = null;
        }
    };

    // Download handler
    const handleDownload = async () => {
        if (!diaryInfo) return;
        try {
            const res = await fetch(`${API_URL}/documents/${studentId}/diary`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const blob = await res.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = diaryInfo.filename || "internship-diary.pdf";
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Error downloading diary:", error);
        }
    };

    // Delete handler
    const handleDelete = async () => {
        if (!window.confirm("Da li ste sigurni da želite da obrišete dnevnik?")) return;
        try {
            const res = await fetch(`${API_URL}/documents/${studentId}/diary`, {
                method: "DELETE",
                headers: { Authorization: `Bearer ${token}` },
            });
            if (res.ok) {
                setDiaryInfo(null);
                alert("Dnevnik obrisan!");
            } else {
                alert("Greška pri brisanju dnevnika");
            }
        } catch (error) {
            console.error("Error deleting diary:", error);
        }
    };

    return (
        <div className="mb-8">

            {diaryInfo ? (
                <div className="border p-4 rounded-lg bg-gray-50">
                    <div className="flex items-center justify-between mb-3">
                        <div>
                            <p className="font-medium">{diaryInfo.filename}</p>
                            <p className="text-sm text-gray-500">
                                Učitano: {new Date(diaryInfo.uploadedAt).toLocaleDateString("sr-RS")}
                            </p>
                        </div>
                        <div className="flex gap-2">
                            <button
                                onClick={handleDownload}
                                className="flex items-center gap-2 text-blue-600 hover:bg-blue-50 px-3 py-2 rounded-lg text-sm"
                            >
                                <Download className="w-4 h-4" /> Preuzmi
                            </button>
                            <button
                                onClick={handleDelete}
                                className="flex items-center gap-2 text-red-600 hover:bg-red-50 px-3 py-2 rounded-lg text-sm"
                            >
                                <Trash2 className="w-4 h-4" /> Obriši
                            </button>
                        </div>
                    </div>

                    <div className="pt-3 border-t border-gray-200">
                        <label className="cursor-pointer text-blue-600 hover:text-blue-700 text-sm font-medium">
                            {uploading ? "Učitavanje..." : "Zameni novi dnevnik"}
                            <input
                                type="file"
                                accept=".pdf"
                                onChange={handleUpload}
                                disabled={uploading}
                                className="hidden"
                            />
                        </label>
                    </div>
                </div>
            ) : (
                <div className="border-2 border-dashed rounded-lg p-6 text-center">
                    {uploading ? (
                        <div className="flex flex-col items-center">
                            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-3"></div>
                            <p className="text-gray-600">Učitavanje dnevnika...</p>
                        </div>
                    ) : (
                        <>
                            <Upload className="w-12 h-12 text-gray-400 mx-auto mb-3" />
                            <label className="cursor-pointer">
                                <span className="text-blue-600 hover:text-blue-700 font-medium">Dodaj dnevnik</span>
                                <input type="file" accept=".pdf" onChange={handleUpload} className="hidden" />
                            </label>
                            <p className="text-sm text-gray-500 mt-2">PDF format, maks 5MB</p>
                        </>
                    )}
                </div>
            )}
        </div>
    );
}