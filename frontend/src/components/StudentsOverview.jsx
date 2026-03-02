import { useEffect, useState } from "react";
import { Users, FileText, XCircle, CheckCircle } from "lucide-react";
import { useAuth } from "../context/AuthContext";

const API_URL = "http://localhost:8080";

export default function Students() {
    const { token, hasRole } = useAuth();
    const [students, setStudents] = useState([]);
    const [selectedStudent, setSelectedStudent] = useState(null);
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [applicationsLoading, setApplicationsLoading] = useState(false);

    // Fetch all students
    useEffect(() => {
        const fetchStudents = async () => {
            try {
                const res = await fetch(`${API_URL}/students`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                const data = await res.json();
                setStudents(data.data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchStudents();
    }, [token]);

    const downloadDiary = async (studentId) => {
        try {
            const res = await fetch(`${API_URL}/documents/${studentId}/diary`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error('Failed to download diary');
            const blob = await res.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = 'internship-diary.pdf';
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
        } catch (err) {
            console.error(err);
        }
    };

    // Fetch applications for selected student
    const fetchApplications = async (studentId) => {
        setApplicationsLoading(true);
        setSelectedStudent(students.find(s => s.id === studentId));
        try {
            const res = await fetch(`${API_URL}/students/${studentId}/applications`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const data = await res.json();
            setApplications(data.data);
        } catch (err) {
            console.error(err);
        } finally {
            setApplicationsLoading(false);
        }
    };

    if (loading) return <p className="text-gray-500 text-center py-12">Učitavanje studenata...</p>;

    return (
        <div className="max-w-6xl mx-auto p-6 flex gap-6">
            {/* Students List */}
            <div className="w-1/3 bg-white border rounded-lg divide-y overflow-y-auto max-h-[70vh]">
                {students.map(student => (
                    <div
                        key={student.id}
                        className={`px-4 py-3 cursor-pointer hover:bg-gray-50 ${selectedStudent?.id === student.id ? "bg-gray-100" : ""}`}
                        onClick={() => fetchApplications(student.id)}
                    >
                        <div className="font-medium text-gray-900">{student.firstName} {student.lastName}</div>
                        <div className="text-sm text-gray-500">{student.email}</div>
                    </div>
                ))}
            </div>

            {/* Applications Panel */}
            <div className="w-2/3 bg-white border rounded-lg p-4 shadow-sm">
                {selectedStudent ? (
                    <>
                        <div className="flex items-center justify-between mb-4">
                            <h3 className="text-xl font-semibold">
                                {selectedStudent.firstName} {selectedStudent.lastName}
                            </h3>
                            {/* download diary button shown only to faculty */}
                            {/**/}
                            {token && selectedStudent && hasRole("FACULTY") && (
                                <button
                                    onClick={() => downloadDiary(selectedStudent.id)}
                                    className="inline-flex items-center px-3 py-1.5 bg-blue-600 text-white text-sm rounded hover:bg-blue-700"
                                >
                                    <FileText className="mr-2 h-4 w-4" />
                                    Preuzmi dnevnik
                                </button>
                            )}
                        </div>

                        {applicationsLoading ? (
                            <p className="text-gray-500">Učitavanje prijava...</p>
                        ) : applications.length ? (
                            <div className="divide-y border-t">
                                {applications.map(app => (
                                    <div key={app.id} className="py-3 flex justify-between items-center px-2 hover:bg-gray-50">
                                        <span className="text-gray-900">{app.internshipTitle}</span>
                                        <span className={`text-sm font-medium ${app.status === "APPROVED" ? "text-green-600" :
                                            app.status === "REJECTED" ? "text-red-600" :
                                                "text-gray-600"
                                            }`}>
                                            {app.status}
                                        </span>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <p className="text-gray-500">Ovaj student nema prijava.</p>
                        )}
                    </>
                ) : (
                    <p className="text-gray-500 text-center py-12">Odaberite studenta da vidite prijave</p>
                )}
            </div>
        </div>
    );
}
