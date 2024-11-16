export interface Employee {
    id: number;
    name: string;
    lastName: string;
    address: string;
    position: string;
    phone: string;
    role: 'MANAGER' | 'EMPLOYEE' | 'SUPERVISOR';
    email: string;
}
