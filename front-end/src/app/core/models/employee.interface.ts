export interface Employee {
    id: number;
    name: string;
    lastName: string;
    address: string;
    position: string;
    role: 'MANAGER' | 'EMPLOYEE';
    email: string;
}
