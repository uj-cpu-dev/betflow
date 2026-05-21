interface ApiResponse<T> {
data: T | null
error: string | null
status: number
}

// Discriminated union — even stronger typing
type ApiResult<T> =
| { success: true;  data: T;      error: null   }
| { success: false; data: null;   error: string }

// Your BetFlow types — match your actual backend responses
interface UserResponse {
id: string
email: string
username: string
role: 'USER' | 'ADMIN'       // union type, not just string
walletBalance: number
createdAt: string
accessToken: string
}

interface WalletResponse {
id: string
balance: number
currency: string
updatedAt: string
}

The 4 utility types you must know — with BetFlow examples
// PARTIAL — makes all fields optional (useful for update payloads)
type UpdateProfileRequest = Partial<UserResponse>
// Now every field is optional — send only what changed

// PICK — select only specific fields from a type
type BetSlipSummary = Pick<BetResponse, 'id' | 'stake' | 'status' | 'potentialPayout'>
// Only those 4 fields — nothing else from BetResponse

// OMIT — exclude specific fields from a type
interface RegisterRequest {
email: string
username: string
password: string
}
// Login doesn't need username — derive it instead of duplicating
type LoginRequest = Omit<RegisterRequest, 'username'>
// Result: { email: string; password: string }

// REQUIRED — makes all fields mandatory (opposite of Partial)
type ConfirmedBet = Required<BetSlipRequest>