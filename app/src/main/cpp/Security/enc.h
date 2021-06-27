
#include <cstddef>
#include <string>
namespace ay
{
    // Obfuscates a string at compile time
    template <std::size_t N, char KEY>
    class obs
    {
    public:
        // Obfuscates the string 'data' on construction
        constexpr obs(const char* data)
        {
            static_assert(KEY != '\0', "KEY must not be the null character.");

            // On construction each of the characters in the string is
            // OBFUSCATE with an XOR cipher based on KEY
            for (std::size_t i = 0; i < N; i++)
            {
                m_data[i] = data[i] ^ KEY;
            }
        }

        constexpr const char* getData() const
        {
            return &m_data[0];
        }

        constexpr std::size_t getSize() const
        {
            return N;
        }

        constexpr char getKey() const
        {
            return KEY;
        }

    private:

        char m_data[N]{};
    };

    // Handles decryption and re-encryption of an encrypted string at runtime
    template <std::size_t N, char KEY>
    class OBS_data
    {
    public:
        OBS_data(const obs<N, KEY>& obs)
        {
            for (std::size_t i = 0; i < N; i++)
            {
                m_data[i] = obs.getData()[i];
            }
        }

        ~OBS_data()
        {
            // Zero m_data to remove it from memory
            for (std::size_t i = 0; i < N; i++)
            {
                m_data[i] = 0;
            }
        }

        // Returns a pointer to the plain text string, decrypting it if
        // necessary
        operator char*()
        {
            decrypt();
            return m_data;
        }
        operator std::string()
        {
            decrypt();
            return m_data;
        }
        // Manually decrypt the string
        void decrypt()
        {
            if (is_encrypted())
            {
                for (std::size_t i = 0; i < N; i++)
                {
                    m_data[i] ^= KEY;
                }
            }
        }

        // Manually re-encrypt the string
        void encrypt()
        {
            if (!is_encrypted())
            {
                for (std::size_t i = 0; i < N; i++)
                {
                    m_data[i] ^= KEY;
                }
            }
        }

        // Returns true if this string is currently encrypted, false otherwise.
        bool is_encrypted() const
        {
            return m_data[N - 1] != '\0';
        }

    private:

        // Local storage for the string. Call is_encrypted() to check whether or
        // not the string is currently OBFUSCATE.
        char m_data[N];
    };

    // This function exists purely to extract the number of elements 'N' in the
    // array 'data'
    template <std::size_t N, char KEY = '.'>
    constexpr auto make_obs(const char(&data)[N])
    {
        return obs<N, KEY>(data);
    }
}

// Obfuscates the string 'data' at compile-time and returns a reference to a
// ay::OBFUSCATE_data object with global lifetime that has functions for
// decrypting the string and is also implicitly convertable to a char*
#define OBS(data) OBS_KEY(data, '.')

// Obfuscates the string 'data' with 'key' at compile-time and returns a
// reference to a ay::OBFUSCATE_data object with global lifetime that has
// functions for decrypting the string and is also implicitly convertable to a
// char*
#define OBS_KEY(data, key) \
	[]() -> ay::OBS_data<sizeof(data)/sizeof(data[0]), key>& { \
		constexpr auto n = sizeof(data)/sizeof(data[0]); \
		static_assert(data[n - 1] == '\0', "String must be null terminated"); \
		constexpr auto obs = ay::make_obs<n, key>(data); \
		static auto OBS_data = ay::OBS_data<n, key>(obs); \
		return OBS_data; \
	}()
