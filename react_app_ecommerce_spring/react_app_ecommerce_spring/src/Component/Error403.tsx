import { Link } from "react-router-dom";

type Error403Props = {
  supportEmail?: string;
};

export default function Error403({ supportEmail = "piccirillopasquale2017@gmail.com" }: Error403Props) {
  return (
    <main className="min-h-screen bg-gradient-to-b from-gray-50 to-white flex items-center justify-center p-6">
      <div className="max-w-4xl w-full bg-white shadow-md rounded-2xl overflow-hidden grid grid-cols-1 md:grid-cols-2">
        
        {/* Left section - Error message */}
        <div className="p-10 flex items-center justify-center bg-gradient-to-br bg-gray-600 text-white">
          <div className="text-center">
            <h2 className="text-3xl font-extrabold uppercase">Access Denied</h2>
            <p className="mt-2 text-sm opacity-90">You don’t have permission to view this page.</p>
          </div>
        </div>

        {/* Right section - Actions */}
        <div className="p-8 md:p-12 flex flex-col justify-center">
          <span className="text-sm font-semibold text-indigo-600 inline-flex items-center px-3 py-1 rounded-full bg-indigo-50 w-max">
            Error 403 — Forbidden
          </span>

          <div className="mt-6 font-bold leading-tight flex flex-col gap-3">
            <span className="text-4xl text-red-500">Unauthorized</span>
            <span className="block text-2xl">Sorry, you don’t have access to this content.</span>
          </div>

          <p className="mt-4 text-gray-600">
            It seems you’ve tried to open a page that’s restricted. You can:
          </p>

          <ul className="mt-4 list-inside list-disc text-gray-600 space-y-2">
            <li>Return to the homepage</li>
            <li>Contact our support team if you think this is a mistake</li>
          </ul>

          <div className="mt-6 flex flex-col sm:flex-row sm:items-center sm:space-x-3 space-y-3 sm:space-y-0">
            <Link
              to="/"
              className="inline-flex items-center justify-center px-5 py-3 rounded-lg bg-indigo-600 text-white text-sm font-medium shadow hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              Home
            </Link>

            <a
              href={`mailto:${supportEmail}?subject=403%20Access%20Denied`}
              className="inline-flex items-center justify-center px-5 py-3 rounded-lg bg-gray-100 text-gray-700 text-sm font-medium hover:bg-gray-200 focus:outline-none"
            >
              Contact Support
            </a>
          </div>

          <div className="mt-8 text-xs text-gray-400">
            <p>
              <strong>Tip:</strong> If you believe you should have access, try logging in with an account that has the correct permissions.
            </p>
          </div>

          <footer className="mt-6 text-xs text-gray-300">
            <p>403 — Forbidden</p>
          </footer>
        </div>
      </div>
    </main>
  );
}
