import { useState, useEffect } from "react";
import { Briefcase, Calendar, MapPin, FileText, Clock, AlertCircle, Coins } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import InternshipDiary from "./StudenDiaryUpload";

const API_URL = "http://localhost:8080";

export default function MyAcceptedInternship() {
    const { token } = useAuth();

    const [loading, setLoading] = useState(true);
    const [internship, setInternship] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchInternshipData = async () => {
            if (!token) {
                setLoading(false);
                return;
            }

            setLoading(true);
            setError(null);

            try {
                const response = await fetch(`${API_URL}/students/me/accepted-application`, {
                    method: 'GET',
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json"
                    },
                });

                const result = await response.json();

                if (response.ok && result.success && result.data) {
                    setInternship(result.data);
                } else {
                    setError(result.message || "Trenutno nemate prihvaćenu praksu.");
                }
            } catch (err) {
                console.error("Fetch error:", err);
                setError("Došlo je do greške prilikom povezivanja sa serverom.");
            } finally {
                setLoading(false);
            }
        };

        fetchInternshipData();
    }, [token]);

    if (loading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="max-w-4xl mx-auto p-6">
            <div className="mb-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-2">Moja Praksa</h1>
                <p className="text-gray-600">
                    Pregled Vaše aktivne prakse i administracija dnevnika rada.
                </p>
            </div>

            {error ? (
                <div className="bg-amber-50 border border-amber-200 rounded-lg p-6 text-center">
                    <AlertCircle className="w-12 h-12 text-amber-600 mx-auto mb-3" />
                    <p className="text-amber-800 font-medium">{error}</p>
                </div>
            ) : internship ? (
                <div className="space-y-6">
                    <div className="bg-white border border-gray-200 shadow-sm rounded-xl p-6">
                        <div className="flex flex-col md:flex-row md:items-start justify-between mb-6 gap-4">
                            <div>
                                <h2 className="text-2xl font-bold text-gray-900 mb-1">
                                    {internship.internshipTitle}
                                </h2>
                                <p className="text-lg text-blue-600 font-semibold">
                                    {internship.internshipCompanyName}
                                </p>
                            </div>
                            <div className="inline-flex items-center bg-green-100 text-green-800 px-4 py-1.5 rounded-full text-sm font-bold w-fit">
                                <span className="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                                {internship.status}
                            </div>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
                            {/* Lokacija */}
                            <div className="flex items-center gap-3">
                                <div className="p-2 bg-gray-50 rounded-lg">
                                    <MapPin className="w-5 h-5 text-gray-500" />
                                </div>
                                <div>
                                    <p className="text-xs text-gray-500 uppercase tracking-wider font-semibold">Lokacija</p>
                                    <p className="font-medium text-gray-900">{internship.internshipLocation || "Nije navedeno"}</p>
                                </div>
                            </div>

                            {/* Datum prijave */}
                            <div className="flex items-center gap-3">
                                <div className="p-2 bg-gray-50 rounded-lg">
                                    <FileText className="w-5 h-5 text-gray-500" />
                                </div>
                                <div>
                                    <p className="text-xs text-gray-500 uppercase tracking-wider font-semibold">Prijavljeno dana</p>
                                    <p className="font-medium text-gray-900">{new Date(internship.appliedAt).toLocaleDateString("sr-RS")}</p>
                                </div>
                            </div>
                        </div>

                        {internship.internshipDescription && (
                            <div className="border-t border-gray-100 pt-6">
                                <h3 className="text-sm font-bold text-gray-800 mb-2 uppercase tracking-tight">Opis zaduženja</h3>
                                <p className="text-gray-600 leading-relaxed italic">
                                    "{internship.internshipDescription}"
                                </p>
                            </div>
                        )}
                    </div>

                    <div className="bg-white border border-gray-200 shadow-sm rounded-xl p-6">
                        <div className="flex items-center gap-3 mb-2">
                            <FileText className="w-6 h-6 text-blue-600" />
                            <h3 className="text-xl font-bold text-gray-900">Dnevnik Prakse</h3>
                        </div>
                        <p className="text-gray-500 mb-6 text-sm">
                        </p>

                        {internship.studentId && (
                            <div className="bg-gray-50 rounded-lg p-4 border-2 border-dashed border-gray-200">
                                <InternshipDiary studentId={internship.studentId} />
                            </div>
                        )}
                    </div>
                </div>
            ) : null}
        </div>
    );
}